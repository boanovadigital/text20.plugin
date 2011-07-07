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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.util;

import java.awt.Point;
import java.io.Serializable;

import net.jcores.shared.utils.VanillaUtil;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationUtil;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;

/**
 * Some functions regarding {@link Saccade}s. 
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public final class SaccadeUtil extends VanillaUtil<Saccade> implements Saccade, Cloneable, Serializable {

    /** */
    private static final long serialVersionUID = -1549243749037784971L;

    /**
     * Constructs a new saccade util.
     * 
     * @param saccade  
     */
    public SaccadeUtil(final Saccade saccade) {
        super(saccade);
    }

    /**
     * Returns the angle of this saccade in polar coordinates.
     * 
     * @return Value 0 means right, +pi/2 means up, pi means left, -pi/2 means down.  
     */
    public double getAngle() {
        return calculateAngle(this.object.getStart(), this.object.getEnd());
    }

    /**
     * Returns the length of this saccade.
     * 
     * @return The lenght in pixels.
     */
    public double getLength() {
        return calculateDistance(this.object.getStart(), this.object.getEnd());
    }

    /**
     * Caluclates the distance between two {@link Fixation}s.
     * 
     * @param f1 Fixation 1.
     * @param f2 Fixation 2.
     * @return The distance between both fixation in pixels.
     */
    public final static double calculateDistance(final Fixation f1, final Fixation f2) {
        final Point c1 = f1.getCenter();
        final Point c2 = f2.getCenter();

        return c1.distance(c2);
    }

    /**
     * Calculates the angle between two fixations.
     * 
     * @param f1 Fixation 1.
     * @param f2 Fixation 2.
     * 
     * @return The return value is given in polar coordinates from c1 to c2. 0 means right, +pi/2 means up, pi means left, -pi/2 means down.   
     */
    public final static double calculateAngle(final Fixation f1, final Fixation f2) {
        final Point c1 = f1.getCenter();
        final Point c2 = f2.getCenter();

        return Math.atan2(c2.getY() - c1.getY(), c2.getX() - c1.getX());
    }

    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade#getEnd()
     */
    @Override
    public FixationUtil getEnd() {
        return new FixationUtil(this.object.getEnd());
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade#getStart()
     */
    @Override
    public FixationUtil getStart() {
        return new FixationUtil(this.object.getEnd());
    }
}
