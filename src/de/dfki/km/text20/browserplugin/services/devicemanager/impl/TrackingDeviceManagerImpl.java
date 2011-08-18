/*
 * TrackingDeviceManagerImpl.java
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

import static net.jcores.jre.CoreKeeper.$;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import net.xeoh.plugins.base.PluginInformation;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import net.xeoh.plugins.diagnosis.local.Diagnosis;
import net.xeoh.plugins.diagnosis.local.DiagnosisChannel;
import net.xeoh.plugins.diagnosis.local.options.status.OptionInfo;
import net.xeoh.plugins.informationbroker.InformationBroker;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.devicemanager.brokeritems.devices.EyeTrackingDeviceItem;
import de.dfki.km.text20.browserplugin.services.devicemanager.diagnosis.channels.tracing.TrackingDeviceManagerTracer;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;

/**
 * Manages the tracking device
 *
 * @author Ralf Biedert
 */
@PluginImplementation
public final class TrackingDeviceManagerImpl implements TrackingDeviceManager {

    /** */
    private static final String ENVIRONMENT_URL = "environment://";

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

    /** Responsible for tracing messages */
//    final DiagnosisChannel<String> diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(TrackingDeviceManagerTracer.class);

    /**
     * @param args
     */
    public static void main(final String[] args) {
        $(TrackingDeviceManagerImpl.class.getResourceAsStream("mouse.wav")).audio().play();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * de.dfki.km.augmentedtext.browserplugin.services.devicemanager.TrackingDeviceManager
     * #initTrackerConnection(java.lang.String, java.lang.String)
     */
    @Override
    public EyeTrackingDevice initEyeTrackerConnection(final String _deviceSelector, final String _trackerConnection) {
        final DiagnosisChannel<String> diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(TrackingDeviceManagerTracer.class);


        diagnosis.status("initeyetrackerconnection/start", new OptionInfo("_deviceSelector", _deviceSelector), new OptionInfo("_trackerConnection", _trackerConnection));

        boolean autoDetection = false;

        // Setup device
        String deviceSelector = _deviceSelector;
        String trackerConnection = _trackerConnection;

        diagnosis.status("initeyetrackerconnection/set/connection");

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

        diagnosis.status("initeyetrackerconnection/connection/mode", new OptionInfo("deviceSelector", deviceSelector), new OptionInfo("autoDetection", Boolean.valueOf(autoDetection)));

        // Check if we should get our connection from the environment
        if (trackerConnection.startsWith(ENVIRONMENT_URL)) {
            diagnosis.status("initeyetrackerconnection/get/connection", new OptionInfo("trackerConnection", trackerConnection));

            final String getenv = System.getenv(trackerConnection.substring(ENVIRONMENT_URL.length()));

            diagnosis.status("initeyetrackerconnection/connection/environment", new OptionInfo("getenv", getenv));

            trackerConnection = getenv;

            if (trackerConnection == null) {
                diagnosis.status("initeyetrackerconnection/get/connection/mouse");
                deviceSelector = "eyetrackingdevice:mouse";
                trackerConnection = "";
            }
        }

        diagnosis.status("initeyetrackerconnection/get/device", new OptionInfo("deviceSelector", deviceSelector));

        // Obtain the proper tracking device here
        EyeTrackingDeviceProvider deviceProvider = this.pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities(deviceSelector));

        // No device is bad ... at this stage ...
        if (deviceProvider == null) {
            diagnosis.status("initeyetrackerconnection/deviceProvider/unusual", new OptionInfo("deviceSelector", deviceSelector));
            return null;
        }

        diagnosis.status("initeyetrackerconnection/connecting", new OptionInfo("trackerConnection", trackerConnection));

        // Now open the device
        this.eyeTrackingDevice = deviceProvider.openDevice(trackerConnection);

        // If opening the device didn't work and we have autodetection, use the mouse
        if (this.eyeTrackingDevice == null && autoDetection) {

            diagnosis.status("initeyetrackerconnection/connecting/mouse");

            deviceSelector = "eyetrackingdevice:mouse";

            // Obtain the fallback device here
            deviceProvider = this.pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities(deviceSelector));

            // No device is bad ... at this stage ...
            if (deviceProvider == null) {
                diagnosis.status("initeyetrackerconnection/connecting/nodevice/unusual", new OptionInfo("deviceSelector", deviceSelector));
                return null;
            }

            // Now open the fallback device
            this.eyeTrackingDevice = deviceProvider.openDevice(trackerConnection);

            // Bad luck today.
            if (this.eyeTrackingDevice == null) {
                diagnosis.status("initeyetrackerconnection/connecting/nodevice/unusual");
                return null;
            }
        }

        // Check if we have a "true" tracking device unrelated to the mouse
        if (deviceSelector.equals("eyetrackingdevice:trackingserver") && this.eyeTrackingDevice != null) {
            this.infoBroker.publish(EyeTrackingDeviceItem.class, this.eyeTrackingDevice);
            $(TrackingDeviceManagerImpl.class.getResourceAsStream("tracker.wav")).audio().play();
        } else {
            $(TrackingDeviceManagerImpl.class.getResourceAsStream("mouse.wav")).audio().play();
        }

        addDataRateListener();

        diagnosis.status("initeyetrackerconnection/end");

        return this.eyeTrackingDevice;
    }

    private void addDataRateListener() {
        final DiagnosisChannel<String> diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(TrackingDeviceManagerTracer.class);

        diagnosis.status("adddataratelistener/call");

        if (this.eyeTrackingDevice == null) return;

        final AtomicLong lng = new AtomicLong();
        final AtomicBoolean warned = new AtomicBoolean(false);

        diagnosis.status("adddataratelistener/register");
        this.eyeTrackingDevice.addTrackingListener(new EyeTrackingListener() {
            @Override
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

                final long delta = current - old;

                if (delta > 2000 && !warned.get()) {
                    diagnosis.status("adddataratelistener/timer/lowdatarate", new OptionInfo("delta", Long.valueOf(delta)));
                    $(TrackingDeviceManagerImpl.class.getResourceAsStream("dataratelow.wav")).audio().play();
                    warned.set(true);
                }
            }

        }, 2500, 2500);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager#
     * initBrainTrackerConnection(java.lang.String, java.lang.String)
     */
    @Override
    public BrainTrackingDevice initBrainTrackerConnection(final String _deviceSelector, final String _trackerConnection) {
        final DiagnosisChannel<String> diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(TrackingDeviceManagerTracer.class);

        diagnosis.status("initbraintrackerconnection/start", new OptionInfo("_deviceSelector", _deviceSelector), new OptionInfo("_trackerConnection", _trackerConnection));

        diagnosis.status("initbraintrackerconnection/set/connection");

        // Setup device
        final String trackerConnection = (_trackerConnection == null) ? "discover://any" : _trackerConnection;

        // Obtain the proper tracking device here
        BrainTrackingDeviceProvider deviceProvider = this.pluginManager.getPlugin(BrainTrackingDeviceProvider.class);

        // No device is bad ... at this stage ...
        if (deviceProvider == null) {
            diagnosis.status("initbraintrackerconnection/connection/nobraintracker");
            return null;
        }

        diagnosis.status("initbraintrackerconnection/connecting/braintracker", new OptionInfo("trackerConnection", trackerConnection));

        // Now open the device
        this.brainTrackingDevice = deviceProvider.openDevice(trackerConnection);

        // If opening the device didn't work and we have autodetection, use the mouse
        if (this.brainTrackingDevice == null) {
            diagnosis.status("initbraintrackerconnection/connecting/braintracker/unusual");
            return null;
        }

        diagnosis.status("initbraintrackerconnection/end");

        return this.brainTrackingDevice;
    }
}
