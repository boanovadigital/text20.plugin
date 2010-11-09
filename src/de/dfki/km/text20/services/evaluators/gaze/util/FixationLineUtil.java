/*
 * FixationLineUtil.java
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;

/**
 * Some mathematical functions regarding fixation lines 
 * 
 * @author rb
 *
 */
public class FixationLineUtil {

    /** */
    private final FixationLine fixationLine;

    /** */
    private final FixationsUtil fixationsUtil;

    /**
     * @param fixationLine  
     */
    public FixationLineUtil(final FixationLine fixationLine) {
        if (fixationLine == null) throw new IllegalArgumentException();

        this.fixationLine = fixationLine;
        this.fixationsUtil = new FixationsUtil(this.fixationLine.getFixations());
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getAverageYPosition()
     */
    public int getAverageYPosition() {
        return this.fixationsUtil.getAverageYPosition();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getAvgSaccadeLength()
     */
    public int getAvgSaccadeLength() {
        return this.fixationsUtil.getAvgSaccadeLength();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getAvgVerticalDeviation()
     */
    public int getAvgVerticalDeviation() {
        return this.fixationsUtil.getAvgVerticalDeviation();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getDimension()
     */
    public Dimension getDimension() {
        return this.fixationsUtil.getDimension();
    }

    /**
     * @param screenRectangle
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getGazetimeFor(java.awt.Rectangle)
     */
    public long getGazetimeFor(final Rectangle screenRectangle) {
        return this.fixationsUtil.getGazetimeFor(screenRectangle);
    }

    /**
     * @param n
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getLastFixations(int)
     */
    public List<Fixation> getLastFixations(final int n) {
        return this.fixationsUtil.getLastFixations(n);
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getMinCoordinates()
     */
    public Point getMinCoordinates() {
        return this.fixationsUtil.getMinCoordinates();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getRectangle()
     */
    public Rectangle getRectangle() {
        return this.fixationsUtil.getRectangle();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getStartTime()
     */
    public long getStartTime() {
        return this.fixationsUtil.getStartTime();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getStopTime()
     */
    public long getStopTime() {
        return this.fixationsUtil.getStopTime();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getAllAngles()
     */
    public double[] getAllAngles() {
        return this.fixationsUtil.getAllAngles();
    }

    /** 
     * @return .
     * @see de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil#getMedianYPosition()
     */
    public int getMedianYPosition() {
        return this.fixationsUtil.getMedianYPosition();
    }

}
