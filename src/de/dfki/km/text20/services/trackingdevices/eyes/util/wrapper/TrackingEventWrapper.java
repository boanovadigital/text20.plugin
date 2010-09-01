/*
 * TrackingEventWrapper.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper;

import java.awt.Point;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 * Wraps tracking events and allows changing of its values.
 * 
 * @author rb
 */
public class TrackingEventWrapper implements EyeTrackingEvent {

    final EyeTrackingEvent originalEvent;

    /**
     * Wraps the given event.
     * 
     * @param trackingEvent
     */
    public TrackingEventWrapper(final EyeTrackingEvent trackingEvent) {
        this.originalEvent = trackingEvent;
    }

    public boolean areValid(final EyeTrackingEventValidity... validities) {
        return this.originalEvent.areValid(validities);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#eventTime()
     */
    public long getEventTime() {
        return this.originalEvent.getEventTime();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getGazeCenter()
     */
    public Point getGazeCenter() {
        return (Point) this.originalEvent.getGazeCenter().clone();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getHeadPosition()
     */
    public float[] getHeadPosition() {
        return this.originalEvent.getHeadPosition();
    }

    public float getLeftEyeDistance() {
        return this.originalEvent.getLeftEyeDistance();
    }

    public float[] getLeftEyePosition() {
        return this.originalEvent.getLeftEyePosition();
    }

    public float getPupilSizeLeft() {
        return this.originalEvent.getPupilSizeLeft();
    }

    public float getPupilSizeRight() {
        return this.originalEvent.getPupilSizeRight();
    }

    public float getRightEyeDistance() {
        return this.originalEvent.getRightEyeDistance();

    }

    public float[] getRightEyePosition() {
        return this.originalEvent.getRightEyePosition();
    }

    public Point getLeftEyeGazePoint() {
        return this.originalEvent.getLeftEyeGazePoint();
    }

    public float[] getLeftEyeGazePosition() {
        return this.originalEvent.getLeftEyePosition();
    }

    public Point getRightEyeGazePoint() {
        return this.originalEvent.getRightEyeGazePoint();
    }

    public float[] getRightEyeGazePosition() {
        return this.originalEvent.getRightEyeGazePosition();
    }
}
