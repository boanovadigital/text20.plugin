/*
 * SaccadeUtil.java
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
package de.dfki.km.text20.services.evaluators.gaze.util;

import java.awt.Point;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;

/**
 * Some functions regarding saccades 
 * 
 * @author rb
 *
 */
public final class SaccadeUtil {

    /** */
    private final Saccade saccade;

    /**
     * @param saccade  
     */
    public SaccadeUtil(final Saccade saccade) {
        this.saccade = saccade;
    }

    /**
     * Returns the angle of this saccade in polar coordinates.
     * 
     * @return Value 0 means right, +pi/2 means up, pi means left, -pi/2 means down.  
     */
    public double getAngle() {
        return calculateAngle(this.saccade.getStart(), this.saccade.getEnd());
    }

    /**
     * Returns the length of this saccade.
     * 
     * @return .
     */
    public double getLength() {
        return calculateDistance(this.saccade.getStart(), this.saccade.getEnd());
    }

    /**
     * @param f1
     * @param f2
     * @return .
     */
    public final static double calculateDistance(final Fixation f1, final Fixation f2) {
        final Point c1 = f1.getCenter();
        final Point c2 = f2.getCenter();

        return c1.distance(c2);
    }

    /**
     * Calculates the angle between two fixations.
     * 
     * @param f1
     * @param f2
     * 
     * @return The return value is given in polar coordinates from c1 to c2. 0 means right, +pi/2 means up, pi means left, -pi/2 means down.   
     */
    public final static double calculateAngle(final Fixation f1, final Fixation f2) {
        final Point c1 = f1.getCenter();
        final Point c2 = f2.getCenter();

        return Math.atan2(c2.getY() - c1.getY(), c2.getX() - c1.getX());
    }

}
