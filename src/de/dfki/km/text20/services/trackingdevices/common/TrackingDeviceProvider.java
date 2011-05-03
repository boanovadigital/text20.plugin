/*
 * TrackingDeviceProvider.java
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
package de.dfki.km.text20.services.trackingdevices.common;

import net.xeoh.plugins.base.Plugin;

/**
 * Provides tracking devices.
 * 
 * @author Ralf Biedert
 * @since 1.0
 * @param <I> The type of the {@link TrackingDeviceInfo}.
 * @param <T> The type of the {@link TrackingEvent}.
 * @param <L> The type of the {@link TrackingListener}.
 * @param <D> The type of the {@link TrackingDevice}.
 *
 */
public interface TrackingDeviceProvider<I extends TrackingDeviceInfo, T extends TrackingEvent, L extends TrackingListener<T>, D extends TrackingDevice<I, T, L>>
        extends Plugin {

    /**
     * Opens a connection to the device. This usually opens a network connection and starts 
     * polling (or listening to) its data
     *  
     * @param url URL to connect to. Not all devices require this, some accept null.
     * 
     * @return Connection a to the device.
     */
    D openDevice(String url);
}
