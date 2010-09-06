/*
 * TrackingDeviceManager.java
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
package de.dfki.km.text20.browserplugin.services.devicemanager.impl;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import net.xeoh.plugins.base.PluginInformation;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.standarditems.vanilla.VanillaItem;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;
import de.dfki.km.text20.util.rd3party.sound.AePlayWave;

/**
 * Manages the tracking device
 * 
 * @author rb
 *
 */
@PluginImplementation
public final class TrackingDeviceManagerImpl implements TrackingDeviceManager {

    /** */
    private static final String ENVIRONMENT_URL = "environment://";

    /**
     * @param args
     */
    public static void main(final String[] args) {
        new AePlayWave(TrackingDeviceManagerImpl.class.getResourceAsStream("mouse.wav")).start();
    }

    /** */
    @InjectPlugin
    public InformationBroker infoBroker;

    /** */
    @InjectPlugin
    public PluginInformation pluginInformation;

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /** Used device */
    private EyeTrackingDevice eyeTrackingDevice;

    /** Used brain tracking device */
    private BrainTrackingDevice brainTrackingDevice;

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.devicemanager.TrackingDeviceManager#getTrackingDevice()
     */
    public EyeTrackingDevice getEyeTrackingDevice() {
        return this.eyeTrackingDevice;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.devicemanager.TrackingDeviceManager#initTrackerConnection(java.lang.String, java.lang.String)
     */
    public void initEyeTrackerConnection(final String _deviceSelector,
                                         final String _trackerConnection) {

        boolean autoDetection = false;

        // Setup device
        String deviceSelector = _deviceSelector;
        String trackerConnection = _trackerConnection;

        this.logger.info("Setting up tracker connection");

        if (deviceSelector == null) {
            deviceSelector = "eyetrackingdevice:mouse";
        } else if (_deviceSelector.equals("eyetrackingdevice:auto")) {
            // Auto means try server first
            deviceSelector = "eyetrackingdevice:trackingserver";
            autoDetection = true;
        }

        if (trackerConnection == null) {
            trackerConnection = "discover://any";
        }

        this.logger.info("Device is now " + deviceSelector);
        this.logger.info("Auto detecion is set to " + autoDetection);

        // Check if we should get our connection from the environment 
        if (trackerConnection.startsWith(ENVIRONMENT_URL)) {
            this.logger.info("Getting connection from " + trackerConnection);
            final String getenv = System.getenv(trackerConnection.substring(ENVIRONMENT_URL.length()));
            this.logger.info("Connection is " + getenv);
            trackerConnection = getenv;

            if (trackerConnection == null) {
                this.logger.warning("Environment not set! Using mouse fallback!");
                deviceSelector = "eyetrackingdevice:mouse";
                trackerConnection = "";
            }
        }

        this.logger.info("Getting device with selector " + deviceSelector);

        // Obtain the proper tracking device here
        EyeTrackingDeviceProvider deviceProvider = this.pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities(deviceSelector));

        // No device is bad ... at this stage ...
        if (deviceProvider == null) {
            this.logger.warning("No tracking device found for " + deviceSelector + "!");
            return;
        }

        this.logger.info("Device found, opening connection to " + trackerConnection);

        // Now open the device
        this.eyeTrackingDevice = deviceProvider.openDevice(trackerConnection);

        // If opening the device didn't work and we have autodetection, use the mouse
        if (this.eyeTrackingDevice == null && autoDetection) {

            this.logger.info("Device did not open. Trying mouse.");

            deviceSelector = "eyetrackingdevice:mouse";

            // Obtain the fallback device here
            deviceProvider = this.pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities(deviceSelector));

            // No device is bad ... at this stage ...
            if (deviceProvider == null) {
                this.logger.warning("Still no tracking device found for " + deviceSelector + ". This is really bad.");
                return;
            }

            // Now open the fallback device
            this.eyeTrackingDevice = deviceProvider.openDevice(trackerConnection);

            // Bad luck today.
            if (this.eyeTrackingDevice == null) {
                this.logger.warning("No device found. This is terminal.");
                return;
            }
        }

        // Check if we have a "true" tracking device unrelated to the mouse
        if (deviceSelector.equals("eyetrackingdevice:trackingserver") && this.eyeTrackingDevice != null) {
            this.infoBroker.publish(new VanillaItem<EyeTrackingDevice>("eyetracking:truedevice", this.eyeTrackingDevice));
            new AePlayWave(getClass().getResourceAsStream("tracker.wav")).start();
        } else {
            new AePlayWave(getClass().getResourceAsStream("mouse.wav")).start();
        }

        addDataRateListener();
    }

    private void addDataRateListener() {
        this.logger.fine("Adding datarate listener");

        if (this.eyeTrackingDevice == null) return;

        final AtomicLong lng = new AtomicLong();
        final AtomicBoolean warned = new AtomicBoolean(false);

        this.eyeTrackingDevice.addTrackingListener(new EyeTrackingListener() {
            public void newTrackingEvent(final EyeTrackingEvent event) {
                lng.set(System.currentTimeMillis());
            }
        });

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                // Check how old the last event is. If it's too old, warn
                final long current = System.currentTimeMillis();
                final long old = lng.get();

                if (current - old > 2000 && !warned.get()) {
                    TrackingDeviceManagerImpl.this.logger.warning("Low datarate detected");
                    new AePlayWave(getClass().getResourceAsStream("dataratelow.wav")).start();
                    warned.set(true);
                }
            }

        }, 2500, 2500);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager#getBTrackingDevice()
     */
    @Override
    public BrainTrackingDevice getBrainTrackingDevice() {
        return this.brainTrackingDevice;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager#initBrainTrackerConnection(java.lang.String, java.lang.String)
     */
    @Override
    public void initBrainTrackerConnection(String _deviceSelector,
                                           String _trackerConnection) {

        // Setup device
        String trackerConnection = _trackerConnection;

        this.logger.info("Setting up tracker connection");

        if (trackerConnection == null) {
            trackerConnection = "discover://any";
        }

        // Obtain the proper tracking device here
        BrainTrackingDeviceProvider deviceProvider = this.pluginManager.getPlugin(BrainTrackingDeviceProvider.class);

        // No device is bad ... at this stage ...
        if (deviceProvider == null) {
            this.logger.warning("No brain tracker device provide found");
            return;
        }

        this.logger.info("Device found, opening connection to " + trackerConnection);

        // Now open the device
        this.brainTrackingDevice = deviceProvider.openDevice(trackerConnection);

        // If opening the device didn't work and we have autodetection, use the mouse
        if (this.brainTrackingDevice == null) {
            this.logger.info("Device did not open. No brainz today.");
            return;
        }
    }
}
