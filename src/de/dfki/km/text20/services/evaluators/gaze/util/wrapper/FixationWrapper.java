/*
 * FixationWrapper.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.wrapper;

import java.awt.Point;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Wraps a {@link Fixation}, can be used to quickly override a method for a given fixation. 
 *
 * @author Ralf Biedert
 * @since 1.0
 */
public class FixationWrapper implements Fixation {

    final Fixation originalFixation;

    /**
     * Wraps the given event.
     *
     * @param originalFixation The fixation to wrap.
     */
    public FixationWrapper(final Fixation originalFixation) {
        this.originalFixation = originalFixation;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.listenertypes.fixation.Fixation#getCenter()
     */
    @Override
    public Point getCenter() {
        return this.originalFixation.getCenter();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.listenertypes.fixation.Fixation#getTrackingEvents()
     */
    @Override
    public List<EyeTrackingEvent> getTrackingEvents() {
        return this.originalFixation.getTrackingEvents();
    }
}
