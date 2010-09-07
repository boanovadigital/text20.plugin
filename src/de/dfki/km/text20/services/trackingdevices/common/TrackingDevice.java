/*
 * TrackingDevice.java
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

/**
 * Represents a tracking device.
 * 
 * @author Ralf Biedert
 * 
 * @param <I> 
 * @param <T> 
 * @param <L> 
 */
public interface TrackingDevice<I extends TrackingDeviceInfo, T extends TrackingEvent, L extends TrackingListener<T>> {
    /**
     * Adds a tracking listener to the device.
     * 
     * @param listener
     */
    public void addTrackingListener(L listener);

    /**
     * Returns info about the tracking device.
     * 
     * @return .
     */
    public I getDeviceInfo();

    /**
     * Closes the device.
     */
    public void closeDevice();

}
