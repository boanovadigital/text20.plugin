/*
 * FixationUtil.java
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

import static net.jcores.CoreKeeper.$;

import java.awt.Point;
import java.util.Date;
import java.util.List;

import net.xeoh.plugins.base.util.VanillaUtil;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Additional functions regarding {@link Fixation}s. 
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public final class FixationUtil extends VanillaUtil<Fixation> {

    /** Tracking events */
    private final List<EyeTrackingEvent> trackingEvents;

    /**
     * Creates a new wrapper.
     * 
     * @param fixation
     */
    public FixationUtil(final Fixation fixation) {
        super(fixation);
        this.trackingEvents = $(fixation.getTrackingEvents()).list();
    }

    /**
     * Return the date when this fixation ended.
     * 
     * @return The end date.
     */
    public Date getEndDate() {
        if (this.trackingEvents.size() == 0) return null;

        return new Date(this.trackingEvents.get(this.trackingEvents.size() - 1).getEventTime());
    }

    /**
     * Returns the time in milliseconds this fixation lasted.
     * 
     * @return The duration.
     */
    public long getFixationDuration() {
        if (this.trackingEvents.size() == 0) return 0;

        return getEndDate().getTime() - getStartDate().getTime();
    }

    /**
     * Returns maximal derivation in pixels from this fixation's center.
     * 
     * @return The maximal derivation.
     */
    public int getMaxCenterDerivation() {
        final Point center = this.object.getCenter();

        float maxDist = 0;

        for (final EyeTrackingEvent trackingEvent : this.trackingEvents) {
            final Point gazeCenter = trackingEvent.getGazeCenter();

            maxDist = (float) Math.max(maxDist, gazeCenter.distance(center));
        }

        return (int) maxDist;
    }

    /**
     * Returns the mean derivation in pixels from the fixation's center.
     * 
     * @return The mean derivation.
     */
    public int getMeanCenterDerivation() {
        final Point center = this.object.getCenter();

        float dist = 0;

        for (final EyeTrackingEvent trackingEvent : this.trackingEvents) {
            final Point gazeCenter = trackingEvent.getGazeCenter();

            dist += center.distance(gazeCenter);
        }

        return (int) (dist / this.trackingEvents.size());
    }

    /**
     * Return the date when this fixation ended.
     * 
     * @return The end date.
     */
    public Date getStartDate() {
        if (this.trackingEvents.size() == 0) return null;

        return new Date(this.trackingEvents.get(0).getEventTime());
    }
}
