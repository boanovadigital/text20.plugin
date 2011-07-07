/*
 * LastObservations.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.impl.trackingserver;

/**
 * @author Ralf Biedert
 *
 */
public class LastObservations {
    /** Last proper position values of the right eye */
    public float lastLeft[] = new float[3];

    /** Last proper position values of the right eye */
    public float lastRight[] = new float[3];

    /** */
    public long dateOfLeft = 0;

    /** */
    public long dateOfRight = 0;

    /** */
    public float lastDeltas[] = new float[3];
}
