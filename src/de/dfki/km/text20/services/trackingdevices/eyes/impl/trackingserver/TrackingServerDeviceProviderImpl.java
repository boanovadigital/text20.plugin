/*
 * TrackingServerDeviceProviderImpl.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.impl.trackingserver;

import java.awt.Point;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.xeoh.plugins.base.PluginConfiguration;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.diagnosis.local.util.DiagnosisChannelUtil;
import net.xeoh.plugins.diagnosis.local.util.DiagnosisUtil;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.remote.RemoteAPILipe;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceType;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;
import de.dfki.km.text20.services.trackingdevices.eyes.diagnosis.channels.tracer.EyeTrackingDeviceTracer;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingClientCallback;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingCommand;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingDeviceInformation;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingEvent;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingServerRegistry;
import de.dfki.km.text20.trackingserver.eyes.remote.options.SendCommandOption;

/**
 * 
 * @author rb
 * 
 */
@PluginImplementation
public class TrackingServerDeviceProviderImpl implements EyeTrackingDeviceProvider {

    private class ServerTrackingDevice implements EyeTrackingDevice,
            TrackingClientCallback {

        /** */
        TrackingDeviceInformation deviceInformation;

        /** Indicates if the client is properly connected */
        final boolean isProperlyConnected;

        /** Manages acces to the listener. */
        final Lock listenerLock = new ReentrantLock();

        /** List of listeners we inform. */
        final List<EyeTrackingListener> trackingListener = new ArrayList<EyeTrackingListener>();

        /** */
        final TrackingServerRegistry registry;

        /** Last proper position values of the right eye */
        final LastObservations last = new LastObservations();

        /**
         * @param string
         * @throws URISyntaxException
         * 
         */
        public ServerTrackingDevice(final String string) throws URISyntaxException {
            // Get remote proxy of the server
            final DiagnosisChannelUtil<String> channel = TrackingServerDeviceProviderImpl.this.diagnosis.channel(EyeTrackingDeviceTracer.class);
            channel.status("servertrackingdevice/start", "url", string);
            
            channel.status("servertrackingdevice/getregistry");            
            this.registry = TrackingServerDeviceProviderImpl.this.remoteAPI.getRemoteProxy(new URI(string), TrackingServerRegistry.class);
            channel.status("servertrackingdevice/getregistry/obtained");            


            if (this.registry == null) {
                this.isProperlyConnected = false;
                return;
            }

            this.isProperlyConnected = true;

            channel.status("servertrackingdevice/getinfo");
            this.deviceInformation = this.registry.getTrackingDeviceInformation();
            channel.status("servertrackingdevice/getinfo/obtained");
            
            this.registry.addTrackingListener(this);
            
            channel.status("servertrackingdevice/end");
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * addTrackingListener
         * (de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener)
         */
        @Override
        public void addTrackingListener(final EyeTrackingListener listener) {
            if (!this.isProperlyConnected) { return; }
            this.listenerLock.lock();
            try {
                this.trackingListener.add(listener);
            } finally {
                this.listenerLock.unlock();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * getDeviceInfo()
         */
        @Override
        public EyeTrackingDeviceInfo getDeviceInfo() {
            return new EyeTrackingDeviceInfo() {

                @Override
                public String getInfo(final String key) {
                    if (ServerTrackingDevice.this.deviceInformation == null) return null;

                    if (key.equals("DEVICE_NAME"))
                        return ServerTrackingDevice.this.deviceInformation.deviceName;
                    if (key.equals("DEVICE_MANUFACTURER"))
                        return ServerTrackingDevice.this.deviceInformation.trackingDeviceManufacturer;
                    if (key.equals("HARDWARE_ID"))
                        return ServerTrackingDevice.this.deviceInformation.hardwareID;

                    return null;
                }

                @Override
                public String[] getKeys() {
                    return new String[] { "DEVICE_NAME", "HARDWARE_ID", "DEVICE_MANUFACTURER" };
                }

                @SuppressWarnings("unused")
                public int getTrackingEventRate() {
                    // TODO Auto-generated method stub
                    return 0;
                }
            };
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * getDeviceType()
         */
        @Override
        public EyeTrackingDeviceType getDeviceType() {
            return EyeTrackingDeviceType.TRACKER;
        }

        /**
         * @return the isProperlyConnected
         */
        public boolean isProperlyConnected() {
            return this.isProperlyConnected;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.trackingserver.remote.TrackingClientCallback
         * #newTrackingEvent(de.dfki.km.augmentedtext.trackingserver.remote.
         * TrackingEvent)
         */
        @Override
        public void newTrackingEvent(final TrackingEvent e) {
            // Sometimes null events might occur. Filter them.
            if (e == null) return;

            // Convert class to 'interface'
            final de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent trackingEvent = new de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent() {

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #areValid(de.dfki.km.augmentedtext.services.trackingdevices.
                 * TrackingEventValidity[])
                 */
                @Override
                public boolean areValid(final EyeTrackingEventValidity... validities) {
                    boolean rval = true;
                    for (final EyeTrackingEventValidity v : validities) {
                        if (v == EyeTrackingEventValidity.CENTER_POSITION_VALID) {
                            rval &= e._centerValidity;
                        }
                    }
                    return rval;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #eventTime()
                 */
                @Override
                public long getEventTime() {
                    return e.date;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #getGazeCenter()
                 */
                @Override
                public Point getGazeCenter() {
                    if (e.centerGaze == null) return new Point(-1, -1);
                    return new Point(e.centerGaze.x, e.centerGaze.y);
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #getHeadPosition()
                 */
                @Override
                public float[] getHeadPosition() {
                    final float rval[] = new float[3];
                    final float invalid[] = new float[] { -1, -1, -1 };

                    boolean leftEyeFound = true;
                    boolean rightEyeFound = true;

                    final float leftEyePos[] = (e.leftEyePos != null) ? e.leftEyePos : invalid;
                    final float rightEyePos[] = (e.rightEyePos != null) ? e.rightEyePos : invalid;

                    // First check if all of the head coordinates are okay.
                    for (int i = 0; i < 3; i++) {

                        // Update the backup value if the current value is in
                        // valid range
                        if (!(leftEyePos[i] > 0.0f && leftEyePos[i] < 1.0f)) {
                            leftEyeFound = false;
                        }

                        if (!(rightEyePos[i] > 0.0f && rightEyePos[i] < 1.0f)) {
                            rightEyeFound = false;
                        }
                    }

                    // Check if we got both eyes ...
                    if (leftEyeFound && rightEyeFound) {
                        // If everything is fine, update the last deltas
                        for (int i = 0; i < 3; i++) {
                            ServerTrackingDevice.this.last.lastDeltas[i] = rightEyePos[i] - leftEyePos[i];
                            ServerTrackingDevice.this.last.lastLeft[i] = leftEyePos[i];
                            ServerTrackingDevice.this.last.lastRight[i] = rightEyePos[i];
                        }

                        ServerTrackingDevice.this.last.dateOfLeft = e.date;
                        ServerTrackingDevice.this.last.dateOfRight = e.date;

                        // Update rval
                        for (int i = 0; i < 3; i++) {
                            rval[i] = (leftEyePos[i] + rightEyePos[i]) / 2;
                        }

                        // And return
                        return rval;
                    }

                    // In case we have only the left eye ....
                    if (leftEyeFound) {
                        // Update last deltas
                        for (int i = 0; i < 3; i++) {
                            ServerTrackingDevice.this.last.lastLeft[i] = leftEyePos[i];
                        }

                        ServerTrackingDevice.this.last.dateOfLeft = e.date;

                        // Update rval
                        for (int i = 0; i < 3; i++) {
                            rval[i] = (leftEyePos[i] + ServerTrackingDevice.this.last.lastDeltas[i] / 2);
                        }

                        // And return
                        return rval;
                    }

                    // Or the right eye ....
                    if (rightEyeFound) {
                        // Update last deltas
                        for (int i = 0; i < 3; i++) {
                            ServerTrackingDevice.this.last.lastRight[i] = rightEyePos[i];
                        }

                        ServerTrackingDevice.this.last.dateOfRight = e.date;

                        // Update rval
                        for (int i = 0; i < 3; i++) {
                            rval[i] = (rightEyePos[i] - ServerTrackingDevice.this.last.lastDeltas[i] / 2);
                        }

                        // And return
                        return rval;
                    }

                    // In this case, no eye was found, so we select the latest
                    // position
                    if (ServerTrackingDevice.this.last.dateOfLeft > ServerTrackingDevice.this.last.dateOfRight) {
                        for (int i = 0; i < 3; i++) {
                            rval[i] = (ServerTrackingDevice.this.last.lastLeft[i] - ServerTrackingDevice.this.last.lastDeltas[i] / 2);
                        }

                    } else {
                        for (int i = 0; i < 3; i++) {
                            rval[i] = (ServerTrackingDevice.this.last.lastRight[i] + ServerTrackingDevice.this.last.lastDeltas[i] / 2);
                        }
                    }

                    return rval;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see java.lang.Object#toString()
                 */
                @Override
                public float getLeftEyeDistance() {
                    return e.eyeDistances[0];
                }

                @Override
                public float[] getLeftEyePosition() {
                    return e.leftEyePos;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #pupilSizeLeft()
                 */
                @Override
                public float getPupilSizeLeft() {
                    return e.pupilSizeLeft;
                }

                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent
                 * #pupilSizeRight()
                 */
                @Override
                public float getPupilSizeRight() {
                    return e.pupilSizeRight;
                }

                @Override
                public float getRightEyeDistance() {
                    return e.eyeDistances[1];
                }

                @Override
                public float[] getRightEyePosition() {
                    return e.rightEyePos;
                }

                @Override
                public String toString() {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    sb.append(getEventTime());
                    sb.append(": ");
                    sb.append(getGazeCenter());
                    sb.append("]");
                    return sb.toString();
                }

                @Override
                public Point getLeftEyeGazePoint() {
                    return e.leftGaze;
                }

                @Override
                public float[] getLeftEyeGazePosition() {
                    return e.gazeLeftPos;
                }

                @Override
                public Point getRightEyeGazePoint() {
                    return e.rightGaze;
                }

                @Override
                public float[] getRightEyeGazePosition() {
                    return e.gazeRightPos;
                }
            };

            // Lock listeners ...
            ServerTrackingDevice.this.listenerLock.lock();

            // TODO: Decouple the follwing calls from this method, otherwise we
            // might block our caller ...

            try {
                // And dispatch it to the listener
                for (int i = 0; i < ServerTrackingDevice.this.trackingListener.size(); i++) {
                    final EyeTrackingListener l = ServerTrackingDevice.this.trackingListener.get(i);
                    try {
                        l.newTrackingEvent(trackingEvent);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        TrackingServerDeviceProviderImpl.this.diagnosis.channel(EyeTrackingDeviceTracer.class).status("event/dispatch/exception", "exception", exception.getMessage());
                    }
                }
            } finally {
                ServerTrackingDevice.this.listenerLock.unlock();
            }

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * sendCommand
         * (de.dfki.km.augmentedtext.trackingserver.remote.TrackingCommand,
         * de.dfki
         * .km.augmentedtext.trackingserver.remote.options.SendCommandOption[])
         */
        @Override
        public void sendLowLevelCommand(TrackingCommand command,
                                        SendCommandOption... options) {
            this.registry.sendCommand(command, options);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * closeDevice()
         */
        @Override
        public void closeDevice() {
            // TODO Auto-generated method stub

        }
    }

    /** */
    @InjectPlugin
    public PluginConfiguration configuration;

    /** */
    @InjectPlugin
    public InformationBroker infobroker;

    /** */
    @InjectPlugin
    public RemoteAPILipe remoteAPI;

    /** Our diagnosis */
    @InjectPlugin
    public DiagnosisUtil diagnosis;

    /**
     * Return what we can do...
     * 
     * @return .
     */
    @Capabilities
    public String[] getCapabilities() {
        return new String[] { "eyetrackingdevice:trackingserver" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDeviceProvider
     * #openDevice(java.lang.String)
     */
    @Override
    public EyeTrackingDevice openDevice(final String url) {
        try {
            ServerTrackingDevice serverTrackingDevice = new ServerTrackingDevice(url);
            if (serverTrackingDevice.isProperlyConnected()) return serverTrackingDevice;

            return null;

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}