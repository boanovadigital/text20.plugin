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
package de.dfki.km.text20.browserplugin.services.devicemanager;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;

/**
 * Manages gaze tracking devices.
 * 
 * @author rb
 *
 */
public interface TrackingDeviceManager extends Plugin {

    /**
     * Returns the tracking device
     * 
     * @return The current tracking device
     *  
     */
    public EyeTrackingDevice getEyeTrackingDevice();

    /**
     * Returns the brain tracking device
     * 
     * @return The current tracking device
     *  
     */
    public BrainTrackingDevice getBrainTrackingDevice();

    /**
     * Inits the tracker connection using a given identifier.
     * 
     * @param deviceSelector
     * @param trackerConnection
     */
    public void initEyeTrackerConnection(String deviceSelector, String trackerConnection);

    /**
     * Inits the tracker connection using a given identifier.
     * 
     * @param deviceSelector
     * @param trackerConnection
     */
    public void initBrainTrackerConnection(String deviceSelector, String trackerConnection);
}