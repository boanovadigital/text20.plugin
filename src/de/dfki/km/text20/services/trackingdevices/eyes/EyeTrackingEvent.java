/*
 * TrackingEvent.java
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

import java.awt.Point;

import de.dfki.km.text20.services.trackingdevices.common.TrackingEvent;

/**
 * Represents a single gaze event.
 * 
 * TODO: Think of a way to include the original event time and consider different clocks 
 * on the tracking device and this host ... 
 * 
 * TODO: Return gaze positions in float values (as pixel values (212.21) as well as normalized values (0.4361))
 * 
 * @author rb
 */
public interface EyeTrackingEvent extends TrackingEvent {
    /**
     * Check if all given parameters are valid.
     * 
     * @param validities
     * 
     * @return False if one is not valid.
     */
    public boolean areValid(EyeTrackingEventValidity... validities);

    /**
     * Returns the best guess for the current gaze. 
     * 
     * @return Point of the gaze
     */
    public Point getGazeCenter();

    /**
     * Returns a 3-tuple of the head position.
     * 
     * @return [x, y, z]
     */
    public float[] getHeadPosition();

    /**
     * Returns distance of the left eye position.
     * 
     * @return Distance in mm
     */
    public float getLeftEyeDistance();

    /**
     * Returns a 2-tuple of the gaze position of the left eye.
     * 
     * @return [x, y] (values range from 0 ... 1)
     */
    public float[] getLeftEyeGazePosition();

    /**
     * Returns a 2-tuple of the gaze position of the right eye.
     * 
     * @return [x, y] (values range from 0 ... 1)
     */
    public float[] getRightEyeGazePosition();

    /**
     * Returns the gaze point of the right eye
     * 
     * @return .
     */
    public Point getRightEyeGazePoint();

    /**
     * Returns the gaze point of the left eye
     * 
     * @return .
     */
    public Point getLeftEyeGazePoint();

    /**
     * Returns a 3-tuple of the left eye position.
     * 
     * @return [x, y, z]
     */
    public float[] getLeftEyePosition();

    /**
     * Returns the pupil size of the left eye
     * 
     * @return .
     */
    public float getPupilSizeLeft();

    /**
     * Returns the pupil size of the right eye
     * 
     * @return . 
     */
    public float getPupilSizeRight();

    /**
     * Returns distance of the right eye position.
     * 
     * @return Distance in mm
     */
    public float getRightEyeDistance();

    /**
     * Returns a 3-tuple of the right eye position.
     * 
     * @return [x, y, z]
     */
    public float[] getRightEyePosition();

}
