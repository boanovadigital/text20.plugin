/*
 * CentralPointFilter.java
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
package de.dfki.km.text20.util.filter.centralpoint;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper.EyeTrackingEventWrapper;
import de.dfki.km.text20.util.filter.AbstractFilter;

/**
 * Collects functions all centralPoint filters use. 
 * 
 * @author rb
 *
 */
public abstract class CentralPointFilter extends AbstractFilter {

    /** Size of the backlog */
    protected int backlogSize;

    /** All points used to smooth */
    final protected List<Point> lastPoints = new ArrayList<Point>();

    /**
     * Creates the filter with a given backlog size
     * 
     * @param backlogSize
     */
    public CentralPointFilter(final int backlogSize) {
        this.backlogSize = backlogSize;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.AbstractFilter#filterEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public final EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {
        // Cap size
        if (this.lastPoints.size() == this.backlogSize) {
            this.lastPoints.remove(0);
        }

        this.lastPoints.add(event.getGazeCenter());

        // Get point to return
        final Point rval = getPoint();

        // If nothing returned, return the event itself
        if (rval == null) return event;

        return new EyeTrackingEventWrapper(event) {

            @Override
            public Point getGazeCenter() {
                return (Point) rval.clone();
            }
        };
    }

    /**
     * Return the central point to use.
     * 
     * @return
     */
    abstract Point getPoint();

    /**
     * 
     * @param newSize
     */
    public void resize(int newSize) {
        this.backlogSize = newSize;
    }
}
