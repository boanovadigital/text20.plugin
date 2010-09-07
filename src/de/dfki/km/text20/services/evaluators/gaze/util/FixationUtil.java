/*
 * FixationGeometry.java
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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Some mathematical functions about regarding fixations. 
 * 
 * @author rb
 *
 */
public final class FixationUtil {

    /** The fixation of this object. */
    private final Fixation fixation;

    /** Tracking events */
    private final List<EyeTrackingEvent> trackingEvents;

    /**
     * @param fixation
     */
    public FixationUtil(final Fixation fixation) {
        final List<EyeTrackingEvent> te = fixation.getTrackingEvents();

        this.fixation = fixation;
        this.trackingEvents = te == null ? new ArrayList<EyeTrackingEvent>() : te;
    }

    /**
     * Return the date when this fixation ended
     * 
     * @return .
     */
    public Date getEndDate() {
        if (this.trackingEvents.size() == 0) return null;

        return new Date(this.trackingEvents.get(this.trackingEvents.size() - 1).getEventTime());
    }

    /**
     * Returns the time in milliseconds this fixation lasted.
     * 
     * @return .
     */
    public long getFixationDuration() {
        if (this.trackingEvents.size() == 0) return 0;

        return getEndDate().getTime() - getStartDate().getTime();
    }

    /**
     * Returns maximal derivation from this points center.
     * 
     * @return .
     */
    public int getMaxCenterDerivation() {
        final Point center = this.fixation.getCenter();

        float maxDist = 0;

        for (final EyeTrackingEvent trackingEvent : this.trackingEvents) {
            final Point gazeCenter = trackingEvent.getGazeCenter();

            maxDist = (float) Math.max(maxDist, gazeCenter.distance(center));
        }

        return (int) maxDist;
    }

    /**
     * Returns the mean derivation from the fixations center
     * 
     * @return .
     */
    public int getMeanCenterDerivation() {
        final Point center = this.fixation.getCenter();

        float dist = 0;

        for (final EyeTrackingEvent trackingEvent : this.trackingEvents) {
            final Point gazeCenter = trackingEvent.getGazeCenter();

            dist += center.distance(gazeCenter);
        }

        return (int) (dist / this.trackingEvents.size());
    }

    /**
     * Return the date when this fixation started
     * 
     * @return .
     */
    public Date getStartDate() {
        if (this.trackingEvents.size() == 0) return null;

        return new Date(this.trackingEvents.get(0).getEventTime());
    }
}
