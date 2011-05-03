/*
 * TrackingListener.java
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
 * Implement this interface to receive {@link TrackingEvent}s from a {@link TrackingDevice}.
 * 
 * @author Ralf Biedert
 * @since 1.0
 * 
 * @param <T> The type of the {@link TrackingEvent} 
 */
public interface TrackingListener<T extends TrackingEvent> {

    /**
     * Called as soon as a new event arrives.
     * 
     * @param event The new event.
     */
    public void newTrackingEvent(T event);
}
