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
package de.dfki.km.text20.services.trackingdevices.eyes.util;

import java.awt.Point;
import java.io.Serializable;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 * Wraps tracking events and allows changing of its values.
 *
 * @author Ralf Biedert
 */
public class EyeTrackingEventWrapper implements EyeTrackingEvent, Cloneable, Serializable {

    /** */
    private static final long serialVersionUID = -7707604144145893939L;
    
    /** The original event */
    final protected EyeTrackingEvent originalEvent;

    /**
     * Wraps the given event.
     *
     * @param trackingEvent
     */
    public EyeTrackingEventWrapper(final EyeTrackingEvent trackingEvent) {
        this.originalEvent = trackingEvent;
    }

    @Override
    public boolean areValid(final EyeTrackingEventValidity... validities) {
        return this.originalEvent.areValid(validities);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#eventTime()
     */
    @Override
    public long getObservationTime() {
        return this.originalEvent.getObservationTime();
    }
    
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.trackingdevices.common.TrackingEvent#getElapsedTime()
     */
    @Override
    public long getElapsedTime() {
        return this.originalEvent.getElapsedTime();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getGazeCenter()
     */
    @Override
    public Point getGazeCenter() {
        return this.originalEvent.getGazeCenter();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getHeadPosition()
     */
    @Override
    public float[] getHeadPosition() {
        return this.originalEvent.getHeadPosition();
    }

    @Override
    public float getLeftEyeDistance() {
        return this.originalEvent.getLeftEyeDistance();
    }

    @Override
    public float[] getLeftEyePosition() {
        return this.originalEvent.getLeftEyePosition();
    }

    @Override
    public float getPupilSizeLeft() {
        return this.originalEvent.getPupilSizeLeft();
    }

    @Override
    public float getPupilSizeRight() {
        return this.originalEvent.getPupilSizeRight();
    }

    @Override
    public float getRightEyeDistance() {
        return this.originalEvent.getRightEyeDistance();

    }

    @Override
    public float[] getRightEyePosition() {
        return this.originalEvent.getRightEyePosition();
    }

    @Override
    public Point getLeftEyeGazePoint() {
        return this.originalEvent.getLeftEyeGazePoint();
    }

    @Override
    public float[] getLeftEyeGazePosition() {
        return this.originalEvent.getLeftEyePosition();
    }

    @Override
    public Point getRightEyeGazePoint() {
        return this.originalEvent.getRightEyeGazePoint();
    }

    @Override
    public float[] getRightEyeGazePosition() {
        return this.originalEvent.getRightEyeGazePosition();
    }
}
