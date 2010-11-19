/*
 * SessionRecorderImpl.java
 *
 * Copyright (c) 2010, Ralf Biedert, DFKI. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 *
 */
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.configuration.IsDisabled;
import net.xeoh.plugins.informationbroker.InformationBroker;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author rb
 */
@IsDisabled
public class SessionRecorderImpl implements SessionRecorder {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    final Timer autosaveTimer = new Timer();

    volatile Rectangle currentRectangle;

    Timer currentTimer = new Timer();

    final InformationBroker infoBroker;

    Point lastMousePos = new Point();

    Robot robot;

    String sessionDir = "/tmp/session";

    volatile SessionRecordImpl sessionRecord;

    AtomicBoolean started = new AtomicBoolean(false);

    boolean usingOwnMouseListener = false;

    /**
     * 
     * @param pm
     */
    public SessionRecorderImpl(final PluginManager pm) {
        this.infoBroker = pm.getPlugin(InformationBroker.class);

        this.logger.info("Session recorder created");

        init();

        this.autosaveTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                autosave();
            }
        }, 0, 3000);

        // Easiest way.
        initializedOwnMouseCapture();
    }

    /**  */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void autosave() {
        this.logger.fine("Autosave called ... active status " + this.started.get());
        if (this.started.get()) {
            // We need this priviledged stuff for applets...
            AccessController.doPrivileged(new PrivilegedAction() {

                @Override
                public Object run() {
                    if (SessionRecorderImpl.this.sessionRecord == null) return null;
                    SessionRecorderImpl.this.logger.finer("Writing to " + SessionRecorderImpl.this.sessionDir + "/" + "session.xml");
                    SessionRecorderImpl.this.sessionRecord.writeTo(SessionRecorderImpl.this.sessionDir + "/" + "session.xml", true);
                    return null;
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #callFunction(java.lang.String)
     */
    @Override
    public void callFunction(final String function) {
        if (this.sessionRecord == null) return;
        this.sessionRecord.callFunction(function);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #executeJSFunction(java.lang.String, java.lang.Object[])
     */
    @Override
    public void executeJSFunction(final String function, final Object... args) {
        // FIXME: Better record the failed calls and store them separately
        if (this.sessionRecord == null) return;

        String s[] = new String[0];

        if (args != null) {
            s = new String[args.length];
            for (int i = 0; i < s.length; i++) {
                final Object o = args[i];
                s[i] = o.toString();
            }
        }

        this.sessionRecord.executeJSFunction(function, s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #getPreference(java.lang.String, java.lang.String)
     */
    @Override
    public void getPreference(final String key, final String deflt) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.getPreference(key, deflt);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #markLog(java.lang.String)
     */
    @Override
    public void markLog(final String tag) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.markLog(tag);

    }

    @Override
    public void mouseClicked(final int type, final int button) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.mouseClickEvent(type, button);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public void newTrackingEvent(final EyeTrackingEvent event) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.trackingEvent(event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #registerListener(java.lang.String, java.lang.String)
     */
    @Override
    public void registerListener(final String type, final String listener) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.registerListener(type, listener);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #removeListener(java.lang.String)
     */
    @Override
    public void removeListener(final String listener) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.removeListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #setParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setParameter(final String key, final String value) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.putProperty(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #setPreference(java.lang.String, java.lang.String)
     */
    @Override
    public void setPreference(final String key, final String value) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.setPreference(key, value);
    }

    /**
     * Halts the execution of all timers.
     */
    public void shutdown() {
        stop();
        this.autosaveTimer.cancel();
        if (this.currentTimer != null) {
            this.currentTimer.cancel();
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #start()
     */
    @Override
    public void start() {
        // Obtain the session dir (should have been set by now)
        this.sessionDir = "..."; // this.infoBroker.getInformationItem(new
                                 // StringID("global:sessionDir")).getContent();

        // Create sessiondir
        new File(this.sessionDir).mkdirs();

        // Only set started if we were successful.
        this.started.set(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #stop()
     */
    @Override
    public void stop() {
        // We'd rather use autosave ...
        // this.sessionRecord.writeTo(this.sessiondir + "/" + "session.ser");
        this.sessionRecord = null;
    }

    @Override
    public void storeDeviceInfo(final EyeTrackingDeviceInfo deviceInfo) {
        final String[] keys = deviceInfo.getKeys();

        for (final String key : keys) {
            final String value = deviceInfo.getInfo(key);
            setParameter("#deviceinfo." + key, value);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #takeScreenshot()
     */
    @Override
    public void takeScreenshot() {
        if (SessionRecorderImpl.this.sessionRecord == null) return;

        takeScreenshotDelayed(100);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.updateElementFlag(id, flag, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateElementGeometry(java.lang.String, java.lang.String, java.lang.String,
     * java.awt.Rectangle)
     */
    @Override
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final Rectangle r) {
        if (this.sessionRecord == null) return;
        this.sessionRecord.updateElementGeometry(id, type, content, r);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateGeometry(java.awt.Rectangle)
     */
    @Override
    public void updateGeometry(final Rectangle rectangle) {
        if (this.sessionRecord == null) return;
        this.currentRectangle = rectangle;
        this.sessionRecord.updateGeometry(rectangle);
        takeScreenshotDelayed();
    }

    @Override
    public void updateMousePosition(final int x, final int y) {
        if (this.lastMousePos.x != x || this.lastMousePos.y != y) {
            this.lastMousePos.x = x;
            this.lastMousePos.y = y;

            if (this.sessionRecord != null) {
                this.sessionRecord.mouseMovement(x, y);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateViewport(java.awt.Point)
     */
    @Override
    public void updateViewport(final Point viewportStart) {
        if (this.sessionRecord == null) return;

        this.sessionRecord.updateDocumentViewport(viewportStart);
        takeScreenshotDelayed();
    }

    /**
     * Initializes our own mouse tracking
     */
    private void initializedOwnMouseCapture() {
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                SessionRecorderImpl.this.usingOwnMouseListener = true;

                while (true) {
                    final PointerInfo pointerInfo = MouseInfo.getPointerInfo();

                    // Obtain information from the mouse
                    final Point point = pointerInfo.getLocation();

                    updateMousePosition(point.x, point.y);

                    // Sleep some time ...
                    try {
                        Thread.sleep(5);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Takes a screenshot soon.
     */
    private void takeScreenshotDelayed() {
        if (this.sessionRecord == null) return;
        takeScreenshotDelayed(500);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #mouseClicked(int, int)
     */

    /**
     * Takes a screenshot after the given delay.
     * 
     * @param delay
     */
    private void takeScreenshotDelayed(final int delay) {
        if (this.sessionRecord == null) return;

        // Cancel all pending tasks
        try {
            this.currentTimer.cancel();
        } catch (final Throwable e) {
            // Why does the language has to be so crappy ?
        }

        this.currentTimer = new Timer();
        this.currentTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                doTakeScreenshot();

                // Don't invoke us again.
                SessionRecorderImpl.this.currentTimer.cancel();
            }

        }, delay);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateMousePosition(int, int)
     */

    /**
     * Really takes a screenshots.
     */
    void doTakeScreenshot() {
        if (this.sessionRecord == null) return;

        final String file = System.currentTimeMillis() + ".png";
        final String fullpath = SessionRecorderImpl.this.sessionDir + "/" + file;

        // We need this priviledged stuff for applets...
        AccessController.doPrivileged(new PrivilegedAction<BufferedImage>() {

            @Override
            public BufferedImage run() {
                // Try to save the image.
                final BufferedImage createScreenCapture = SessionRecorderImpl.this.robot.createScreenCapture(SessionRecorderImpl.this.currentRectangle);

                try {
                    ImageIO.write(createScreenCapture, "png", new File(fullpath));
                    SessionRecorderImpl.this.sessionRecord.newImage(file);
                } catch (final IOException e) {
                    e.printStackTrace();
                }

                return createScreenCapture;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #storeDeviceInfo
     * (de.dfki.km.augmentedtext.services.trackingdevices.TrackingDeviceInfo)
     */
    void init() {
        try {
            this.robot = new Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.sessionRecord = new SessionRecordImpl(screenSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder
     * #updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(String id, String key, String value) {
        if (this.sessionRecord == null) return;
        this.sessionRecord.updateMetaInformation(id, key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#specialCommand
     * (
     * de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption
     * [])
     */
    @Override
    public void specialCommand(SpecialCommandOption... options) {
        this.logger.warning("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#
     * newBrainTrackingEvent
     * (de.dfki.km.text20.services.braintrackingdevices.BrainTrackingEvent)
     */
    @Override
    public void newBrainTrackingEvent(BrainTrackingEvent event) {
        this.logger.warning("Not implemented");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#
     * storeBrainDeviceInfo
     * (de.dfki.km.text20.services.braintrackingdevices.BrainTrackingDeviceInfo)
     */
    @Override
    public void storeBrainDeviceInfo(BrainTrackingDeviceInfo deviceInfo) {
        final String[] keys = deviceInfo.getKeys();
        for (final String key : keys) {
            final String value = deviceInfo.getInfo(key);
            setParameter("#braindeviceinfo." + key, value);
        }
    }
}
