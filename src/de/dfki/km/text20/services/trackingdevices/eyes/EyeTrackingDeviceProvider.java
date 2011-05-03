/*
 * EyeTrackingDeviceProvider.java
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

import de.dfki.km.text20.services.trackingdevices.common.TrackingDeviceProvider;

/**
 * The EyeTrackingDeviceProvider is a JSPF plugin that, well, provides an {@link EyeTrackingDevice}. In 
 * order to obtain a device, you first have to get the right provider (only two lines of code, see 
 * <a href="http://code.google.com/p/text20/wiki/UsingJavaInterface">this page for details</a>), afterwards you 
 * can use the tracking device to access raw eye tracking data.<br/><br/>
 * 
 * Currently there are two providers:
 * 
 * <ul>
 * <li><code>eyetrackingdevice:trackingserver</code> - Connects to a remote tracking server and 
 * delievers true eye tracking or simulation data.</li>
 * <li><code>eyetrackingdevice:mouse</code> - Works without a tracking server and uses the mouse directly.</li>
 * </ul>
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface EyeTrackingDeviceProvider
        extends
        TrackingDeviceProvider<EyeTrackingDeviceInfo, EyeTrackingEvent, EyeTrackingListener, EyeTrackingDevice> {
    //
}
