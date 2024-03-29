/*
 * DisplacingTrackingEventWrapper.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper;

import java.awt.Point;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventWrapper;

/**
 * @author Ralf Biedert
 *
 */
public class DisplacingEyeTrackingEventWrapper extends EyeTrackingEventWrapper {

    /** */
    private static final long serialVersionUID = -7495619053777400810L;
    
    private final Point displacement;

    /**
     * @param trackingEvent
     * @param displacement
     */
    public DisplacingEyeTrackingEventWrapper(final EyeTrackingEvent trackingEvent,
                                          final Point displacement) {
        super(trackingEvent);

        this.displacement = displacement;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.util.wrapper.TrackingEventWrapper#getGazeCenter()
     */
    @Override
    public Point getGazeCenter() {
        final Point center = this.originalEvent.getGazeCenter();
        center.x += this.displacement.x;
        center.y += this.displacement.y;
        return center;
    }
}
