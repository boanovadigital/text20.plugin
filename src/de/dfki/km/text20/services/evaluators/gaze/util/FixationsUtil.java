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

import static net.jcores.CoreKeeper.$;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.jcores.cores.CoreObject;
import net.jcores.interfaces.functions.F1;
import net.jcores.interfaces.functions.F2DeltaObjects;
import net.jcores.interfaces.functions.F2ReduceObjects;
import net.jcores.utils.Staple;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;

/**
 * Methods regarding fixations.
 *
 * @author rb
 *
 */
public class FixationsUtil {

    /** */
    private final List<Fixation> fixations;

    /** */
    private CoreObject<Fixation> $fixations;

    /**
     * @param fixations
     */
    public FixationsUtil(final List<Fixation> fixations) {
        this.fixations = fixations;
        this.$fixations = $(fixations);
    }

    /**
     * Calculates the average y point of all fixations.
     *
     * @return .
     */
    public int getAverageYPosition() {
        final Staple<Point> staple = this.$fixations.map(new F1<Fixation, Point>() {
            @Override
            public Point f(Fixation arg0) {
                return arg0.getCenter();
            }
        }).staple(new Point(0, 0), new F2ReduceObjects<Point>() {
            @Override
            public Point f(Point arg0, Point arg1) {
                arg0.x += arg1.x;
                arg0.y += arg1.y;
                return arg0;
            }
        });

        return staple.staple().y / staple.size();
    }

    /**
     * TODO: Calculate proper median w. null elements considered.
     *
     * Calculates the average y point of all fixations.
     *
     * @return .
     */
    public int getMedianYPosition() {
        return this.$fixations.map(new F1<Fixation, Point>() {
            @Override
            public Point f(Fixation arg0) {
                return arg0.getCenter();
            }
        }).compact().sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.y - o2.y;
            }
        }).get(0.5).y;
    }

    /**
     * Returns the average length of saccades.
     *
     * @return .
     */
    @SuppressWarnings("boxing")
    public int getAvgSaccadeLength() {
        final Staple<Double> staple = this.$fixations.map(new F1<Fixation, Point>() {
            @Override
            public Point f(Fixation arg0) {
                return arg0.getCenter();
            }
        }).delta(new F2DeltaObjects<Point, Double>() {
            @Override
            public Double f(Point arg0, Point arg1) {
                return arg0.distance(arg1);
            }
        }).staple(0.0, new F2ReduceObjects<Double>() {
            @Override
            public Double f(Double arg0, Double arg1) {
                return arg0 + arg1;
            }
        });

        return (int) (staple.staple() / staple.size());
    }

    /**
     * Returns all angles between the fixations
     *
     * @return .
     */
    @SuppressWarnings("boxing")
    public double[] getAllAngles() {
        CoreObject<Double> fill = this.$fixations.map(new F1<Fixation, Point>() {
            @Override
            public Point f(Fixation arg0) {
                return arg0.getCenter();
            }
        }).delta(new F2DeltaObjects<Point, Double>() {
            @Override
            public Double f(Point c1, Point c2) {
                return Math.atan2(c2.y - c1.y, c2.x - c1.x);
            }
        }).fill(Double.NaN);

        // And now the ugly part: unbox
        final double rval[] = new double[fill.size()];
        for (int i = 0; i < rval.length; i++) {
            rval[i] = fill.get(i);
        }

        return rval;
    }

    /**
     * Returns the avg. vertical deviation of fixations from the
     * whole's line center.
     *
     * @return .
     */
    @SuppressWarnings("boxing")
    public int getAvgVerticalDeviation() {
        final int avg = getAverageYPosition();

        final Staple<Integer> staple = this.$fixations.map(new F1<Fixation, Integer>() {
            @Override
            public Integer f(Fixation x) {
                return x.getCenter().y;
            }
        }).staple(0, new F2ReduceObjects<Integer>() {
            @Override
            public Integer f(Integer left, Integer right) {
                return left + Math.abs(right - avg);
            }
        });

        return staple.staple() / staple.size();
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

            if (center == null) continue;

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

            if (center == null) continue;

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
            if (center == null) continue;
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
        return this.$fixations.compact().get(0).getTrackingEvents().get(0).getEventTime();
    }

    /**
     * Last observation-point of this fixation line.
     *
     * @return .
     */
    public long getStopTime() {
        return $(this.$fixations.compact().get(-1).getTrackingEvents()).compact().get(-1).getEventTime();
    }
}
