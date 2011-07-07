/*
 * MultipleRegionDisplacementFilter.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.filter.displacement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.util.filter.AbstractFilter;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventWrapper;

/**
 * @author Ralf Biedert
 *
 */
public class MultipleRegionDisplacementFilter extends AbstractFilter {

    /** All displaceors we apply */
    final List<Displacer> displacers = new ArrayList<Displacer>();

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.AbstractFilter#filterEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public EyeTrackingEvent filterEvent(EyeTrackingEvent event) {

        final Point originalPoint = event.getGazeCenter();
        Point point = (Point) originalPoint.clone();

        for (Displacer d : this.displacers) {
            point = d.displace(point);
        }

        originalPoint.x -= point.x;
        originalPoint.y -= point.y;

        //if (originalPoint.x != 0 || originalPoint.y != 0)
        //    System.out.println(originalPoint);

        final Point rval = point;

        // Return wrapped point
        return new EyeTrackingEventWrapper(event) {

            /* (non-Javadoc)
             * @see de.dfki.km.augmentedtext.services.trackingdevices.util.wrapper.TrackingEventWrapper#getGazeCenter()
             */
            @Override
            public Point getGazeCenter() {
                return (Point) rval.clone();
            }
        };
    }

    /**
     * @param d
     */
    public void addDisplacer(Displacer d) {
        this.displacers.add(d);
    }
}
