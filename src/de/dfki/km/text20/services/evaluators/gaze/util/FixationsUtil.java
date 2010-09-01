/*
 * FixationsUtil.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Methods regarding fixations.
 * 
 * @author rb
 *
 */
public class FixationsUtil {

    /** */
    private final List<Fixation> fixations;

    /**
     * @param fixations
     */
    public FixationsUtil(final List<Fixation> fixations) {
        this.fixations = fixations;
    }

    /**
     * Calculates the average y point of all fixations. 
     * 
     * @return .
     */
    public int getAverageYPosition() {

        int y = 0;
        for (final Fixation fixation : this.fixations) {
            y += fixation.getCenter().y;
        }

        y /= this.fixations.size();

        return y;
    }

    /**
     * Calculates the average y point of all fixations. 
     * 
     * @return .
     */
    public int getMedianYPosition() {

        int[] ys = new int[this.fixations.size()];

        for (int i = 0; i < this.fixations.size(); i++) {
            ys[i] = this.fixations.get(i).getCenter().y;
        }

        Arrays.sort(ys);

        return ys[ys.length / 2];
    }

    /**
     * Returns the average length of saccades.
     *  
     * @return .
     */
    public int getAvgSaccadeLength() {

        int len = 0;

        final Fixation last = this.fixations.get(0);

        for (final Fixation fixation : this.fixations) {
            len += fixation.getCenter().distance(last.getCenter());
        }

        len /= this.fixations.size();

        return len;
    }

    /**
     * Returns all angles between the fixations
     *  
     * @return .
     */
    public double[] getAllAngles() {
        final double rval[] = new double[this.fixations.size() - 1];

        // Compute angles
        for (int i = 0; i < this.fixations.size() - 1; i++) {
            final Fixation f1 = this.fixations.get(i);
            final Fixation f2 = this.fixations.get(i + 1);

            final Point c1 = f1.getCenter();
            final Point c2 = f2.getCenter();

            double atan2 = Math.atan2(c2.y - c1.y, c2.x - c1.x);
            rval[i] = atan2;
        }

        return rval;
    }

    /**
     * Returns the avg. vertical deviation of fixations from the 
     * whole's line center.
     * 
     * @return .
     */
    public int getAvgVerticalDeviation() {

        int dev = 0;
        final int avg = getAverageYPosition();

        for (final Fixation fixation : this.fixations) {
            dev += Math.abs(fixation.getCenter().y - avg);
        }

        dev /= this.fixations.size();

        return dev;
    }

    /**
     * Returns the fixation line's dimension
     * 
     * @return .
     */
    public Dimension getDimension() {
        final Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        final Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (final Fixation fixation : this.fixations) {
            final Point center = fixation.getCenter();
            min.x = Math.min(min.x, center.x);
            min.y = Math.min(min.y, center.y);

            max.x = Math.max(max.x, center.x);
            max.y = Math.max(max.y, center.y);
        }

        return new Dimension(max.x - min.x, max.y - min.y);
    }

    /**
     * Calculates how long has been gazed into the specified area of screen.
     * 
     * @param screenRectangle
     * @return .
     */
    public long getGazetimeFor(final Rectangle screenRectangle) {
        long rval = 0;

        for (final Fixation fixation : this.fixations) {
            final Point center = fixation.getCenter();

            if (screenRectangle.contains(center)) {
                rval += new FixationUtil(fixation).getFixationDuration();
            }
        }

        return rval;
    }

    /**
     * Returns the last n fixations.
     * @param n
     * @return .
     */
    public List<Fixation> getLastFixations(final int n) {

        final int s = this.fixations.size();
        return new ArrayList<Fixation>(this.fixations.subList(s - n, s));
    }

    /**
     * Returns the minimal x and y values. They are NOT connected. The point is likely not 
     * existent, it only holds that no values are smaller than x or y.
     * 
     * @return .
     */
    public Point getMinCoordinates() {
        final Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (final Fixation fixation : this.fixations) {
            final Point center = fixation.getCenter();
            min.x = Math.min(min.x, center.x);
            min.y = Math.min(min.y, center.y);
        }

        return min;
    }

    /**
     * Returns the rectangle of this fixation line.
     * 
     * @return .
     */
    public Rectangle getRectangle() {
        return new Rectangle(getMinCoordinates(), getDimension());
    }

    /**
     * First observation-point of this event.
     * 
     * @return . 
     */
    public long getStartTime() {
        return this.fixations.get(0).getTrackingEvents().get(0).getEventTime();
    }

    /**
     * Last observation-point of this fixation line.
     * 
     * @return . 
     */
    public long getStopTime() {
        final int fs = this.fixations.size();
        final List<EyeTrackingEvent> trackingEvents = this.fixations.get(fs - 1).getTrackingEvents();
        final int ts = trackingEvents.size();
        return trackingEvents.get(ts - 1).getEventTime();
    }
}
