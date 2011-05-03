/*
 * EyeTrackingDevice.java
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
package de.dfki.km.text20.services.trackingdevices.eyes;

import de.dfki.km.text20.services.trackingdevices.common.TrackingDevice;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingCommand;
import de.dfki.km.text20.trackingserver.eyes.remote.options.SendCommandOption;

/**
 * Represents an eye tracking device that receives {@link EyeTrackingEvent}s. The device can be obtained 
 * through the {@link EyeTrackingDeviceProvider}. This class is your main source of eye tracking data.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface EyeTrackingDevice extends
        TrackingDevice<EyeTrackingDeviceInfo, EyeTrackingEvent, EyeTrackingListener> {

    /**
     * Returns the type of the device.
     * 
     * @return The type.
     */
    public EyeTrackingDeviceType getDeviceType();

    /**
     * Sends a low level tracking command to the device, e.g., to calibrate.
     * 
     * @param command The command to send.
     * @param options The options to add to the command.
     */
    public void sendLowLevelCommand(TrackingCommand command, SendCommandOption... options);
}
