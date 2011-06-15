/*
 * DisplacingFixationWrapper.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.wrapper;

import java.awt.Point;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationWrapper;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Displaces a {@link Fixation}.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class DisplacingFixationWrapper extends FixationWrapper {

    private final Point displacement;

    /**
     * Create a displacer for the given fixation and displacement.
     * 
     * @param originalFixation The fixation.
     * @param displacement The displacement to apply.
     */
    public DisplacingFixationWrapper(final Fixation originalFixation,
                                     final Point displacement) {
        super(originalFixation);
        this.displacement = displacement;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.util.wrapper.FixationWrapper#getCenter()
     */
    @Override
    public Point getCenter() {
        final Point center = this.originalFixation.getCenter();
        center.x += this.displacement.x;
        center.y += this.displacement.y;
        return center;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.gazeevaluator.util.wrapper.FixationWrapper#getTrackingEvents()
     */
    @Override
    public List<EyeTrackingEvent> getTrackingEvents() {
        // TODO: The individual events should be wrapped as well ... However, what to do with the relative view positions?
        return this.originalFixation.getTrackingEvents();
    }
}
