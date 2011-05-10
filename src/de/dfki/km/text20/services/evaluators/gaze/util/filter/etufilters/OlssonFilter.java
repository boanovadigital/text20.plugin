/*
 * OlssonFilter.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.filter.etufilters;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.dfki.km.text20.services.evaluators.gaze.util.filter.AbstractFilter;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper.EyeTrackingEventWrapper;

/**
 * @author Eugen Massini
 *
 */
public class OlssonFilter extends AbstractFilter {

    protected final long highPassMs;
    protected final List<Point> lastPoints = new ArrayList<Point>();

    protected final List<Long> lastTimes = new ArrayList<Long>();
    protected final long lowPassMs;
    protected final int size;
    protected final long threshold;
    protected final long timeWindowMs;

    /**
     * @param size
     * @param timeWindowMs
     * @param lowPassMs
     * @param highPassMs
     * @param threshold
     */
    public OlssonFilter(final int size, final long timeWindowMs, final long lowPassMs,
                        final long highPassMs, final long threshold) {
        this.size = size;
        this.timeWindowMs = timeWindowMs;
        this.lowPassMs = lowPassMs;
        this.highPassMs = highPassMs;
        this.threshold = threshold;
    }

    @Override
    public EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {
        if (this.lastPoints.size() == this.size) {
            this.lastPoints.remove(0);
            this.lastTimes.remove(0);
        }

        this.lastPoints.add(event.getGazeCenter());
        this.lastTimes.add(Long.valueOf(event.getEventTime()));

        final Point resPoint = getPoint();

        if (resPoint == null) return event;

        return new EyeTrackingEventWrapper(event) {

            @Override
            public Point getGazeCenter() {
                return resPoint;
            }
        };
    }

    private final void addPoints(final Point dest, final Point b) {
        dest.x += b.x;
        dest.y += b.y;
    }

    private final void scalePoint(final Point dest, final double v) {
        dest.x *= v;
        dest.y *= v;
    }

    protected final Point getLastPoint() {
        if (this.lastPoints.isEmpty()) return null;
        return this.lastPoints.get(this.lastPoints.size() - 1);
    }

    protected final double getLastTime() {
        if (this.lastTimes.isEmpty()) return 0;
        return this.lastTimes.get(this.lastTimes.size() - 1).doubleValue();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.centralpoint.CentralPointFilter#getPoint()
     */
    Point getPoint() {
        final Point lastPoint = getLastPoint();
        if (lastPoint == null) return null;

        final double lastTime = getLastTime();

        final Point beforeP = new Point(0, 0);
        final Point afterP = new Point(0, 0);
        int beforeC = 0;
        int afterC = 0;

        for (int i = 0; i < this.lastPoints.size(); ++i) {
            final double dTime = lastTime - this.lastTimes.get(i).doubleValue();
            if (dTime <= this.timeWindowMs) {
                addPoints(afterP, this.lastPoints.get(i));
                ++afterC;
            } else if (dTime <= 2 * this.timeWindowMs) {
                addPoints(beforeP, this.lastPoints.get(i));
                ++beforeC;
            }
        }

        scalePoint(beforeP, beforeC);
        scalePoint(afterP, afterC);

        if (afterP.distance(beforeP) < this.threshold) {
            // TODO: lowpass
        } else {
            // TODO: highpass
        }
        throw new NotImplementedException();
    }

}
