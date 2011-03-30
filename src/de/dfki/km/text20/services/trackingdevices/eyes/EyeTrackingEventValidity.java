/*
 * EyeTrackingEventValidity.java
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

/**
 * Are elements valid?
 * 
 * @author Ralf Biedert
 */
public enum EyeTrackingEventValidity {
    /** Is the center position valid? */
    CENTER_POSITION_VALID,

    /** Is the head position valid? */
    HEAD_POSITION_VALID,

    /** Is the left eye position valid */
    LEFT_EYE_POSITION_VALID,

    /** Is the gaze position for the left eye valid */
    LEFT_GAZE_POSITION_VALID,

    /** Is the right eye position valid */
    RIGHT_EYE_POSITION_VALID,

    /** Is the gaze position for the right eye valid */
    RIGHT_GAZE_POSITION_VALID
}
