/*
 * SpakovFilter.java
 * 
 * Copyright (c) 2010, Eugen Massini, DFKI. All rights reserved.
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
package de.dfki.km.text20.util.filter.etufilters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper.TrackingEventWrapper;
import de.dfki.km.text20.util.filter.AbstractFilter;

/**
 * @author Eugen Massini
 *
 */
public class SpakovFilter extends AbstractFilter {

    protected final double distanceFactor;
    protected final double inv2DistanceFactorSq;

    protected final double inv2TimeFactorSq;
    protected final List<Point> lastPoints = new ArrayList<Point>();
    protected final List<Long> lastTimes = new ArrayList<Long>();
    protected final int size;
    protected final double timeFactor;

    /**
     * 
     * @param backlogSize
     * @param timeFactor a nonzero value activates it, zero value deactivates
     * @param distanceFactor a nonzero value activates it, zero value deactivates
     */
    public SpakovFilter(final int backlogSize, final double timeFactor,
                        final double distanceFactor) {
        this.size = backlogSize;
        this.timeFactor = timeFactor;
        this.distanceFactor = distanceFactor;

        this.inv2TimeFactorSq = timeFactor != 0. ? -1. / (2. * timeFactor * timeFactor) : 0.;
        this.inv2DistanceFactorSq = distanceFactor != 0. ? -1. / (2. * distanceFactor * distanceFactor) : 0.;
    }

    @Override
    public final EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {
        if (this.lastPoints.size() == this.size) {
            this.lastPoints.remove(0);
            this.lastTimes.remove(0);
        }

        this.lastPoints.add(event.getGazeCenter());
        this.lastTimes.add(Long.valueOf(event.getEventTime()));

        final Point resPoint = getPoint();

        if (resPoint == null) return event;

        return new TrackingEventWrapper(event) {

            @Override
            public Point getGazeCenter() {
                return resPoint;
            }
        };
    }

    private final Point addPoints(final Point a, final Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    private final void applyPoint(final Point p, final double factor, final Point next) {
        p.x += factor * next.x;
        p.y += factor * next.y;
    }

    private final Point scalePoint(final Point dest, final double v) {
        dest.x *= v;
        dest.y *= v;
        return dest;
    }

    protected final double calcPointFactor(final Point p1, final Point pn) {
        //* is little bit faster than without "if"
        if (isDistanceFactorActive()) return this.inv2DistanceFactorSq * p1.distance(pn);
        return 0.;
        // */
        //return inv2DistanceFactorSq*(p1.distance(pn)); 
    }

    protected final double calcTimeFactor(final double t1, final double tn) {
        //* is little bit faster than without "if"
        if (isTimeFactorActive()) return this.inv2TimeFactorSq * (tn - t1);
        return 0.;
        // */
        //return inv2TimeFactorSq * (tn+t1); 
    }

    protected final Point getLastPoint() {
        if (this.lastPoints.isEmpty()) return null;
        return this.lastPoints.get(this.lastPoints.size() - 1);
    }

    protected final double getLastTime() {
        if (this.lastTimes.isEmpty()) return 0.;
        return this.lastTimes.get(this.lastTimes.size() - 1).doubleValue();
    }

    protected final boolean isDistanceFactorActive() {
        return this.distanceFactor != 0.;
    }

    protected final boolean isTimeFactorActive() {
        return this.timeFactor != 0.;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.centralpoint.CentralPointFilter#getPoint()
     */
    Point getPoint() {
        final Point lastPoint = getLastPoint();
        if (lastPoint == null) return null;

        final double lastTime = getLastTime();

        double sumW = 0.;
        final Point sumPoint = new Point(0, 0);
        for (int i = 0; i < this.lastPoints.size() - 1; ++i) {
            final double w = Math.exp(calcTimeFactor(this.lastTimes.get(i).doubleValue(), lastTime) + calcPointFactor(this.lastPoints.get(i), lastPoint));
            @SuppressWarnings("unused")
            final// just because the class is not yet implemented
            double p = calcPointFactor(this.lastPoints.get(i), lastPoint);
            @SuppressWarnings("unused")
            final// just because the class is not yet implemented
            double t = calcTimeFactor(this.lastTimes.get(i).doubleValue(), lastTime);
            /*
            System.out.print("t "+t);
            System.out.print(" p "+p);
            System.out.println(" w "+(t+p));
            // */
            applyPoint(sumPoint, w, this.lastPoints.get(i));
            sumW += w;
        }

        final Point res = scalePoint(addPoints(lastPoint, sumPoint), 1. / (1. + sumW));
        /*
        System.out.print("sumPoint: "+sumPoint);
        System.out.println(" sumW: "+sumW);
        System.out.println(" res: "+res );
        // */
        return res;
    }
}
