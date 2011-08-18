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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.List;

import net.jcores.jre.CommonCore;
import net.jcores.jre.cores.CoreObject;
import net.jcores.jre.cores.adapter.AbstractAdapter;
import net.jcores.jre.cores.adapter.ListAdapter;
import net.jcores.jre.interfaces.functions.F1;
import net.jcores.jre.interfaces.functions.F2DeltaObjects;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.util.SaccadeDummy;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.util.SaccadesUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Methods regarding as set of {@link Fixation} objects.
 *
 * @author Ralf Biedert
 * @since 1.0
 */
public class FixationsUtil extends CoreObject<Fixation> {
    /** */
    private static final long serialVersionUID = -2953127465640671497L;

    
    /**
     * Creates a new fixations wrapper.
     * 
     * @param fixations The fixations to wrap.
     */
    public FixationsUtil(final List<Fixation> fixations) {
        super($, new ListAdapter<Fixation>(fixations));
    }

    /**
     * Creates a new fixations wrapper.
     * 
     * @param fixations The fixations to wrap.
     */
    public FixationsUtil(final Fixation... fixations) {
        super($, fixations);
    }
    
    /**
     * Creates a new fixations wrapper.
     * 
     * @param c
     * @param adapter
     */
    public FixationsUtil(CommonCore c, AbstractAdapter<Fixation> adapter) {
        super(c, adapter);
    }

    
    /**
     * Computes a pseudo-median point, where the x- and y- coordinates are 
     * computed independently. 
     * 
     * @return A pseudo-median.
     * @since 1.4
     */
    public Point independentAverage() {
        final Point rval = new Point();
        
        int ctr = 0;
        
        for (Point point : map(FixationUtil.fCenter)) {
            rval.x += point.x;
            rval.y += point.y;
            ctr++;
        }
        
        rval.x /= ctr;
        rval.y /= ctr;
        
        return rval;
    }
    
    
    /**
     * Computes a pseudo-average point, where the x- and y- coordinates are 
     * computed independently. 
     * 
     * @return A pseudo-average.
     * @since 1.4
     */
    public Point independentMedian() {
        final Point rval = new Point();
        
        CoreObject<Point> points = map(FixationUtil.fCenter).compact().sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.x - o2.x;
            }
        });
        
        if(points.size() == 0) return null;
        
        rval.x = points.get(0.5).x;

        
        // First sort x-coords.
        points = points.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return o1.y - o2.y;
            }
        });
        rval.y = points.get(0.5).y;
        
        return rval;
    }


    /**
     * Returns all saccades between these fixations.
     *
     * @return An array with all saccades.
     */
    public SaccadesUtil saccades() {
        return delta(new F2DeltaObjects<Fixation, SaccadeDummy>() {
            @Override
            public SaccadeDummy f(Fixation arg0, Fixation arg1) {
                if(arg0 == null || arg1 == null) return null;
                return new SaccadeDummy(arg0, arg1);
            }
        }).as(SaccadesUtil.class);
    }

    
    /**
     * Returns the avg. vertical deviation of fixations from the
     * whole's line center.
     *
     * @return The average deviation in pixels.
     */
    public Dimension standardDeviation() {
        final Point rval = new Point();
        final Point average = independentAverage();
        final Point[] points = map(FixationUtil.fCenter).unsafearray();

        for (Point point : points) {
            rval.x += Math.pow((point.x - average.x), 2);
            rval.y += Math.pow((point.y - average.y), 2);
        }
        
        rval.x /= points.length;
        rval.y /= points.length;
        
        rval.x = (int) Math.sqrt(rval.x);
        rval.y = (int) Math.sqrt(rval.y);
        
        return new Dimension(rval.x, rval.y);
    }

    
    
    /**
     * Calculates how long has been gazed into the specified area of screen.
     *
     * @param screenRectangle The rectangle to query.
     * @return The time in ms.
     */
    public long gazeTimeFor(final Rectangle screenRectangle) {
        long rval = 0;

        // Check for each fixation ... 
        for (final Fixation fixation : this) {
            final Point center = fixation.getCenter();
            if (center == null) continue;

            // If the requested rectangle contained them and add the times
            if (screenRectangle.contains(center)) {
                rval += new FixationUtil(fixation).getFixationDuration();
            }
        }

        // Return the sum
        return rval;
    }

    
    
    /**
     * Returns the rectangle of this fixation line.
     *
     * @return The bounding rectangle.
     */
    public Rectangle boundingRectangle() {
        final Point min = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        final Point max = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        

        map(FixationUtil.fCenter).forEach(new F1<Point, Void>() {
            @Override
            public Void f(Point p) {
                min.x = Math.min(min.x, p.x);
                min.y = Math.min(min.y, p.y);

                max.x = Math.max(max.x, p.x);
                max.y = Math.max(max.y, p.y);

                return null;
            }
        });
      
        return new Rectangle(min.x, min.y, max.x - min.x, max.y - min.y);
    }

    
    
    /**
     * The time of the first measurement of this event.
     *
     * @return The start time.
     */
    public long startTime() {
        final Fixation fixation = compact().get(0);
        if (fixation == null || fixation.getTrackingEvents() == null) return 0;

        final EyeTrackingEvent trackingEvent = fixation.getTrackingEvents().get(0);
        if (trackingEvent == null) return 0;

        return trackingEvent.getObservationTime();
    }

    
    
    /**
     * The time of the last measurement of this event.
     *
     * @return The stop time.
     */
    public long stopTime() {
        final Fixation fixation = compact().get(-1);
        if (fixation == null || fixation.getTrackingEvents() == null) return 0;

        final EyeTrackingEvent trackingEvent = $(fixation.getTrackingEvents()).get(-1);
        if (trackingEvent == null) return 0;

        return trackingEvent.getObservationTime();
    }
    
    /**
     * Returns the duration from the first to the last event.
     * 
     * @return The duration of all the enclosed fixations.  
     */
    public long duration() {
        return stopTime() - startTime();
    }
}
