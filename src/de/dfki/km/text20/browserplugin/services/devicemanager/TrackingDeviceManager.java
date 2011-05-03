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
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;

/**
 * The TrackingDeviceManager tries to locate and connect to existing eye- and 
 * brain tracking devices, either on the same machine or local network. Usually you do not need
 * to access this class, neither when in extension, nor when in library mode (see {@link BrowserAPI}).   
 * 
 * @author Ralf Biedert
 * @since 1.0
 * @see EyeTrackingDevice
 * @see BrainTrackingDevice
 */
public interface TrackingDeviceManager extends Plugin {

    /**
     * Inits the tracker connection using a given identifier.
     * 
     * @param deviceSelector The selector to use, e.g. <code>eyetrackingdevice:trackingserver</code>.
     * @param trackerConnection The location where to locate the device, e.g., <code>discover://nearest</code>.
     * @return Returns the initialized eye tracking device.
     */
    public EyeTrackingDevice initEyeTrackerConnection(String deviceSelector,
                                                      String trackerConnection);

    /**
     * Inits the tracker connection using a given identifier.
     * 
     * @param deviceSelector The selector to use (unused right now).
     * @param trackerConnection The location where to locate the device, e.g., <code>discover://nearest</code>.
     * @return Returns the initialized brain tracking device.
     */
    public BrainTrackingDevice initBrainTrackerConnection(String deviceSelector,
                                                          String trackerConnection);
}