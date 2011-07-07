/*
 * MouseTrackingDeviceProviderImpl.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.impl.mouse;

import static net.jcores.shared.CoreKeeper.$;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Shutdown;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.diagnosis.local.util.DiagnosisUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceType;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;
import de.dfki.km.text20.services.trackingdevices.eyes.diagnosis.channels.tracer.EyeTrackingDeviceTracer;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingCommand;
import de.dfki.km.text20.trackingserver.eyes.remote.options.SendCommandOption;

/**
 * Mouse implementation
 * 
 * @author Ralf Biedert
 */
@PluginImplementation
public class MouseTrackingDeviceProviderImpl implements EyeTrackingDeviceProvider {
    /**
     * Uses the mouse as a tracking device
     * 
     * @author Ralf Biedert
     */
    private class MouseTrackingDevice implements EyeTrackingDevice {
        /** Manages acces to the listener. */
        final Lock listenerLock = new ReentrantLock();

        /** List of listeners we inform. */
        final List<EyeTrackingListener> trackingListener = new ArrayList<EyeTrackingListener>();

        /** */
        Thread thread;

        /**
         * Construct a standard MouseTrackigDevice which will start instantly.
         */
        MouseTrackingDevice() {
            this.thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {

                        final PointerInfo pointerInfo = MouseInfo.getPointerInfo();

                        // Obtain information from the mouse
                        final Point point = pointerInfo.getLocation();

                        final EyeTrackingEvent event = createEvent(point);

                        MouseTrackingDevice.this.listenerLock.lock();
                        // And dispatch it to the listener
                        for (int i = 0; i < MouseTrackingDevice.this.trackingListener.size(); i++) {
                            final EyeTrackingListener l = MouseTrackingDevice.this.trackingListener.get(i);
                            try {
                                l.newTrackingEvent(event);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                MouseTrackingDeviceProviderImpl.this.diagnosis.channel(EyeTrackingDeviceTracer.class).status("event/dispatch/exception", "exception", exception.getMessage());
                            }
                        }
                        MouseTrackingDevice.this.listenerLock.unlock();

                        // Sleep some time ...
                        try {
                            Thread.sleep(50);
                        } catch (final InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }

            });
            this.thread.setDaemon(true);
            this.thread.start();
        }

        /*
         * (non-Javadoc)
         * 
         * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#
         * addTrackingListener
         * (de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener)
         */
        @Override
        public void addTrackingListener(final EyeTrackingListener listener) {
            this.listenerLock.lock();
            try {
                this.trackingListener.add(listener);
            } finally {
                this.listenerLock.unlock();
            }
        }

        public EyeTrackingEvent createEvent(final Point p) {
            return new EyeTrackingEvent() {

                long et = System.currentTimeMillis();

                @Override
                public boolean areValid(final EyeTrackingEventValidity... validities) {
                    return true;
                }

                @Override
                public long getEventTime() {
                    return this.et;
                }

                @Override
                public Point getGazeCenter() {
                    return $.clone(p);
                }

                @Override
                public float[] getHeadPosition() {
                    return new float[] { 0f, 0f, 0.5f };
                }

                @Override
                public float getLeftEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float[] getLeftEyePosition() {
                    return new float[] { 0f, 0f, 0.5f };
                }

                @Override
                public float getPupilSizeLeft() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float getPupilSizeRight() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float getRightEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float[] getRightEyePosition() {
                    return new float[] { 0f, 0f, 0.5f };
                }

                @Override
                public Point getLeftEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public float[] getLeftEyeGazePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Point getRightEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public float[] getRightEyeGazePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }
            };
        }

        @Override
        public EyeTrackingDeviceInfo getDeviceInfo() {
            return new EyeTrackingDeviceInfo() {

                @Override
                public String getInfo(final String key) {
                    return null;
                }

                @Override
                public String[] getKeys() {
                    return new String[0];
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
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#getDeviceType
         * ()
         */
        @Override
        public EyeTrackingDeviceType getDeviceType() {
            return EyeTrackingDeviceType.MOUSE;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#sendCommand
         * (de.dfki.km.augmentedtext.trackingserver.remote.TrackingCommand,
         * de.dfki.km.augmentedtext.trackingserver.remote.options.SendCommandOption[])
         */
        @Override
        public void sendLowLevelCommand(TrackingCommand command,
                                        SendCommandOption... options) {
            // TODO Auto-generated method stub
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice#closeDevice()
         */
        @Override
        public void closeDevice() {
            // TODO Auto-generated method stub

        }
    }

    /** Our diagnosis */
    @InjectPlugin
    public DiagnosisUtil diagnosis;
    
    /** The device we may return */
    volatile MouseTrackingDevice trackingDevice = null;

    /**
     * Return what we can do...
     * 
     * @return .
     */
    @Capabilities
    public String[] getCapabilities() {
        return new String[] { "eyetrackingdevice:mouse" };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.services.trackingdevices.TrackingDeviceProvider#openDevice
     * (java.lang.String)
     */
    @Override
    public EyeTrackingDevice openDevice(final String url) {
        this.diagnosis.channel(EyeTrackingDeviceTracer.class).status("opendevice/start", "url", url);

        synchronized (this) {
            if (this.trackingDevice == null) {
                this.trackingDevice = new MouseTrackingDevice();
            }
        }

        this.diagnosis.channel(EyeTrackingDeviceTracer.class).status("opendevice/end");
        return this.trackingDevice;
    }
    
    @Shutdown
    public void shutdown() {
        if(this.trackingDevice!= null) {
            this.trackingDevice.thread.interrupt();
        }
    }

}
