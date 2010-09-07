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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.util.OptionUtils;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.standarditems.strings.StringID;
import net.xeoh.plugins.informationbroker.util.InformationBrokerUtil;

import com.thoughtworks.xstream.XStream;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.createrecorder.OptionFakeStartDate;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.specialcommand.OptionFakeNextDate;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author rb
 * @author buhl
 *
 */
public class SessionRecorderImpl implements SessionRecorder {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    final Timer autosaveTimer = new Timer();

    /** */
    Timer currentTimer = new Timer();

    /** */
    final InformationBroker infoBroker;

    /** */
    Robot robot;

    /** */
    Point lastMousePos = new Point();

    /** */
    volatile Rectangle currentRectangle;

    /** */
    String sessionDir = "/tmp/session";

    /** */
    volatile SessionStreamer sessionStreamer;

    /** */
    AtomicBoolean started = new AtomicBoolean(false);

    /** */
    XStream xstream;

    /** */
    private final String filenamePrefix = "session.";

    private Date fakeStart;

    /** */
    private static final String filenameExtension = ".xstream";

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#getPreference(java.lang.String, java.lang.String)
     */
    @Override
    public void getPreference(final String key, final String deflt) {

        if (this.sessionStreamer == null) return;
        this.sessionStreamer.getPreference(key, deflt);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#markLog(java.lang.String)
     */
    @Override
    public void markLog(final String tag) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.markLog(tag);

    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#mouseClicked(int, int)
     */
    @Override
    public void mouseClicked(final int type, final int button) {
        if (this.sessionStreamer == null) return;

        this.sessionStreamer.mouseClickEvent(type, button);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#takeScreenshot()
     */
    @Override
    public void takeScreenshot() {
        if (SessionRecorderImpl.this.sessionStreamer == null) return;
        // FIXME: Why do we have the delay when taking screenshots?
        takeScreenshotDelayed(100);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#callFunction(java.lang.String)
     */
    @Override
    public void callFunction(final String function) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.callFunction(function);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#executeJSFunction(java.lang.String, java.lang.Object[])
     */
    @Override
    public void executeJSFunction(final String function, final Object... args) {
        //  FIXME: Better record the failed calls and store them separately
        if (this.sessionStreamer == null) return;

        String s[] = new String[0];

        if (args != null) {
            s = new String[args.length];
            for (int i = 0; i < s.length; i++) {
                final Object o = args[i];
                s[i] = o.toString();
            }
        }

        this.sessionStreamer.executeJSFunction(function, s);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public void newTrackingEvent(final EyeTrackingEvent event) {
        if (this.sessionStreamer == null) return;

        this.sessionStreamer.trackingEvent(event);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#registerListener(java.lang.String, java.lang.String)
     */
    @Override
    public void registerListener(final String type, final String listener) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.registerListener(type, listener);

    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#removeListener(java.lang.String)
     */
    @Override
    public void removeListener(final String listener) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.removeListener(listener);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#setParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setParameter(final String key, final String value) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.putProperty(key, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#setPreference(java.lang.String, java.lang.String)
     */
    @Override
    public void setPreference(final String key, final String value) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.setPreference(key, value);

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

    /* (non-Javadoc)
    * @see de.dfki.km.augmentedtext.browserplugin.services.eventrecorder.SessionStreamer#start()
    */
    @Override
    public void start() {

        // Obtain the session dir (should have been set by now)
        // Return the session dir
        this.sessionDir = new InformationBrokerUtil(this.infoBroker).getItem(new StringID("global:sessionDir"), "/tmp/");

        // Create sessiondir
        new File(this.sessionDir).mkdirs();

        // Create streamer
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.sessionStreamer = new SessionStreamer(screenSize, createFileName(), this.fakeStart);

        // Only set started if we were successful.
        this.started.set(true);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.eventrecorder.SessionStreamer#stop()
     */
    @Override
    public void stop() {
        // We'd rather use autosave ...
        // this.sessionRecord.writeTo(this.sessiondir + "/" + "session.ser");
        this.sessionStreamer = null;
    }

    /**
     * @param deviceInfo
     */
    @Override
    public void storeDeviceInfo(final EyeTrackingDeviceInfo deviceInfo) {
        final String[] keys = deviceInfo.getKeys();
        for (final String key : keys) {
            final String value = deviceInfo.getInfo(key);
            setParameter("#deviceinfo." + key, value);
        }
    }

    /**
     *
     * @param pm
     * @param options
     */
    public SessionRecorderImpl(final PluginManager pm, CreateRecorderOption... options) {
        this.infoBroker = pm.getPlugin(InformationBroker.class);

        try {
            this.xstream = new XStream();
        } catch (Exception e) {
            this.logger.warning("Error creating xstream! No logging available!.");
            e.printStackTrace();
            this.xstream = null;
        }

        final OptionUtils<CreateRecorderOption> ou = new OptionUtils<CreateRecorderOption>(options);
        if (ou.contains(OptionFakeStartDate.class)) {
            this.fakeStart = new Date(ou.get(OptionFakeStartDate.class).getStartDate());
        }

        this.logger.fine("Session streamer created");

        init();
    }

    /** */
    void init() {

        try {
            this.robot = new Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }

        try {
            SessionStreamer.setAlias(this.xstream);
        } catch (Exception e) {
            this.logger.warning("Error setting alias. No session recorder available!");
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.eventrecorder.SessionStreamer#updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(final String id, final String key, final String value) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.updateMetaInformation(id, key, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.updateElementFlag(id, flag, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#updateElementGeometry(java.lang.String, java.lang.String, java.lang.String, java.awt.Rectangle)
     */
    @Override
    public void updateElementGeometry(final String id, final String type, final String content, final Rectangle r) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.updateElementGeometry(id, type, content, r);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#updateGeometry(java.awt.Rectangle)
     */
    @Override
    public void updateGeometry(final Rectangle rectangle) {
        if (this.sessionStreamer == null) return;
        this.currentRectangle = rectangle;
        this.sessionStreamer.updateGeometry(rectangle);
        takeScreenshotDelayed();
    }

    @Override
    public void updateMousePosition(final int x, final int y) {

        if (this.lastMousePos.x != x || this.lastMousePos.y != y) {
            this.lastMousePos.x = x;
            this.lastMousePos.y = y;

            if (this.sessionStreamer != null) {
                this.sessionStreamer.mouseMovement(x, y);
            }
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorder#updateViewport(java.awt.Point)
     */
    @Override
    public void updateViewport(final Point viewportStart) {
        if (this.sessionStreamer == null) return;
        this.sessionStreamer.updateDocumentViewport(viewportStart);
        takeScreenshotDelayed();
    }

    /**
     * Takes a screenshot after the given delay.
     *
     * @param delay
     */
    private void takeScreenshotDelayed(final int delay) {
        if (this.sessionStreamer == null) return;

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

    /**
     * Really takes a screenshots.
     */
    void doTakeScreenshot() {
        if (this.sessionStreamer == null) return;

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
                    SessionRecorderImpl.this.sessionStreamer.newImage(file);
                } catch (final IOException e) {
                    e.printStackTrace();
                }

                return createScreenCapture;
            }
        });
    }

    /**
     * Takes a screenshot soon.
     */
    private void takeScreenshotDelayed() {
        if (this.sessionStreamer == null) return;
        takeScreenshotDelayed(500);
    }

    /**
     * @return generated file path:
     * [session_directory]/[filePrefix]_[startTimeInMilliSeconds].[filenameExtension]
     */
    private String createFileName() {
        return this.sessionDir + "/" + this.filenamePrefix + System.currentTimeMillis() + filenameExtension;
    }

    /**
     * @return .
     */
    public String getSessionDir() {
        return this.sessionDir;
    }

    /**
     * @param sessionDir
     */
    public void setSessionDir(String sessionDir) {
        this.sessionDir = sessionDir;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#specialCommand(de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption[])
     */
    @Override
    public void specialCommand(SpecialCommandOption... options) {
        final OptionUtils<SpecialCommandOption> ou = new OptionUtils<SpecialCommandOption>(options);
        if (ou.contains(OptionFakeNextDate.class)) {
            long next = ou.get(OptionFakeNextDate.class).getDate();
            this.sessionStreamer.nextDate(new Date(next));
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#newBrainTrackingEvent(de.dfki.km.text20.services.braintrackingdevices.BrainTrackingEvent)
     */
    @Override
    public void newBrainTrackingEvent(BrainTrackingEvent event) {
        if (this.sessionStreamer == null) return;

        this.sessionStreamer.brainTrackingEvent(event);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder#storeBrainDeviceInfo(de.dfki.km.text20.services.braintrackingdevices.BrainTrackingDeviceInfo)
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
