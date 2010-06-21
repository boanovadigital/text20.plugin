/*
 * GazerecorderImpl.java
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
package de.dfki.km.text20.browserplugin.services.onscreenmenu.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.configuration.IsDisabled;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.standarditems.integer.IntegerItem;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.onscreenmenu.OnScreenMenu;
import de.dfki.km.text20.browserplugin.services.onscreenmenu.impl.widgets.RecalibrationWidget;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceType;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;

/**
 * @author rb
 *
 */
@IsDisabled
@PluginImplementation
public class OnScreeenMenuImpl implements OnScreenMenu, EyeTrackingListener {

    /** */
    @InjectPlugin
    public TrackingDeviceManager deviceManager;

    /** */
    @InjectPlugin
    public PluginManager facade;

    /** */
    @InjectPlugin
    public InformationBroker infobroker;

    /** */
    public EyeTrackingDevice trackingDevice;

    /** Fullscreen overlay */
    DashboardFrame dashboardFrame = new DashboardFrame();

    /** */
    boolean enableable = true;

    /** When did we enter the upper left corner */
    long entered = Long.MAX_VALUE;

    /** Are we showing something */
    boolean isShowing = false;

    /** */
    RecalibrationWidget recalibrationWidget;

    /** Did we already make a connection to the true device? */
    boolean trueTrackingDeviceEnabled = false;

    /** */
    @Init
    public void init() {
        initMouseListener();
        addWidgets();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener#newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    public void newTrackingEvent(final EyeTrackingEvent event) {
        final Point p = event.getGazeCenter();
        final long ct = System.currentTimeMillis();
        if (p.x == 0 && p.y == 0) {
            if (this.entered + 1000 < ct && !this.isShowing && this.enableable) {
                showMenu();
            }

            this.entered = Math.min(this.entered, ct);
        } else {
            this.entered = Long.MAX_VALUE;
            if (this.isShowing) {
                hideMenu();
            }
        }

    }

    private void addWidgets() {
        this.recalibrationWidget = new RecalibrationWidget() {
            /** */
            private static final long serialVersionUID = -6400226293766024451L;

            @Override
            public void finished() {
                updateResults();
            }
        };

        this.dashboardFrame.getFrameContainer().add(this.recalibrationWidget);
    }

    private void initMouseListener() {
        // Hack: we use the tracking device to access the mouse coordinates. 
        final EyeTrackingDeviceProvider plugin = this.facade.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities("eyetrackingdevice:mouse"));
        this.trackingDevice = plugin.openDevice(null);
        this.trackingDevice.addTrackingListener(new EyeTrackingListener() {

            public void newTrackingEvent(final EyeTrackingEvent event) {
                if (!event.areValid(EyeTrackingEventValidity.CENTER_POSITION_VALID)) return;

                final Point p = event.getGazeCenter();
                final long ct = System.currentTimeMillis();
                if (p.x == 0 && p.y == 0) {
                    if (OnScreeenMenuImpl.this.entered + 1000 < ct && !OnScreeenMenuImpl.this.isShowing && OnScreeenMenuImpl.this.enableable) {
                        showMenu();
                    }

                    OnScreeenMenuImpl.this.entered = Math.min(OnScreeenMenuImpl.this.entered, ct);
                } else {
                    OnScreeenMenuImpl.this.entered = Long.MAX_VALUE;
                    if (OnScreeenMenuImpl.this.isShowing) {
                        hideMenu();
                    }
                }

                if (OnScreeenMenuImpl.this.recalibrationWidget != null) {
                    OnScreeenMenuImpl.this.recalibrationWidget.newGazePoint(event);
                }

                if (OnScreeenMenuImpl.this.dashboardFrame != null) {
                    OnScreeenMenuImpl.this.dashboardFrame.addGazePoint(event.getGazeCenter());
                }
            }

        });

    }

    private void initTrueTrackingDevice() {
        if (this.trueTrackingDeviceEnabled) return;

        // Obtain the "primary" tracking device
        final EyeTrackingDevice primaryDevice = this.deviceManager.getEyeTrackingDevice();

        // Is it a true tracker?
        if (!primaryDevice.getDeviceType().equals(EyeTrackingDeviceType.TRACKER)) return;

        // Setup device listener
        primaryDevice.addTrackingListener(new EyeTrackingListener() {

            public void newTrackingEvent(final EyeTrackingEvent event) {
                // Return if the center position is invalid
                if (!event.areValid(EyeTrackingEventValidity.CENTER_POSITION_VALID)) return;

                OnScreeenMenuImpl.this.recalibrationWidget.newGazePoint(event);
                OnScreeenMenuImpl.this.dashboardFrame.addGazePoint(event.getGazeCenter());
            }
        });
    }

    void hideMenu() {
        this.isShowing = false;
        this.dashboardFrame.setVisible(false);

        updateResults();
    }

    void showMenu() {
        initTrueTrackingDevice();

        // Reset calibration (TODO: Maybe not the best solution)
        this.infobroker.publish(new IntegerItem("tracking:recalibration:x", 0));
        this.infobroker.publish(new IntegerItem("tracking:recalibration:y", 0));

        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Dimension screenSize = tk.getScreenSize();

        final int W = 800;
        final int H = 600;

        this.recalibrationWidget.setBounds(new Rectangle(screenSize.width / 2 - W / 2, screenSize.height / 2 - H / 2, W, H));
        this.recalibrationWidget.setVisible(true);
        this.recalibrationWidget.start();

        this.dashboardFrame.setVisible(true);

        this.isShowing = true;
    }

    void updateResults() {
        // Check if the calibration performed normally
        final Point calibrationOffset = this.recalibrationWidget.getCalibrationOffset();
        if (calibrationOffset != null) {
            this.infobroker.publish(new IntegerItem("tracking:recalibration:x", calibrationOffset.x));
            this.infobroker.publish(new IntegerItem("tracking:recalibration:y", calibrationOffset.y));

        }
    }
}
