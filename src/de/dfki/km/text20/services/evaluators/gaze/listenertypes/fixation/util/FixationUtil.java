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

import static net.jcores.shared.CoreKeeper.$;

import java.awt.Point;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.jcores.shared.interfaces.functions.F1;
import net.jcores.shared.utils.VanillaUtil;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Additional functions regarding {@link Fixation}s. 
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public final class FixationUtil extends VanillaUtil<Fixation> implements Fixation, Cloneable, Serializable {
    /** jCores Function to get the center of a fixation */
    public static F1<Fixation, Point> fCenter = new F1<Fixation, Point>() {
        @Override
        public Point f(Fixation x) {
            return x.getCenter();
        }
    };
    
    /** jCores Function to get the tracking events of a fixation */
    public static F1<Fixation, List<EyeTrackingEvent>> fEvents = new F1<Fixation, List<EyeTrackingEvent>>() {
        @Override
        public List<EyeTrackingEvent> f(Fixation x) {
            return x.getTrackingEvents();
        }
    };

    
    /** */
    private static final long serialVersionUID = 6821731469615255876L;
    
    /** Tracking events */
    private final List<EyeTrackingEvent> trackingEvents;

    /**
     * Creates a new wrapper.
     * 
     * @param fixation
     */
    public FixationUtil(final Fixation fixation) {
        super(fixation);
        
        if(fixation == null) {
            this.trackingEvents = $.list();
        } else {
            this.trackingEvents = $(fixation.getTrackingEvents()).list();
        }
    }

    /**
     * Return the date when this fixation ended.
     * 
     * @return The end date.
     */
    public Date getEndDate() {
        if (this.trackingEvents.size() == 0) return null;

        return new Date(this.trackingEvents.get(this.trackingEvents.size() - 1).getObservationTime());
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

        return new Date(this.trackingEvents.get(0).getObservationTime());
    }
    

    /**
     * Returns the similarity between two fixations, a value between 0 and 1. Fixations are similar
     * when they had a similar length and were at a similar position. Similarity is computed
     * relative to the given spatial and temporal scale. 
     *   
     *   
     * @param fixation The fixation to compare the current with.
     * @param dSpacial The spacial scale. I.e., when the scale is 50px, then 25px delta are treated as 0.5.
     * @param dTemporal The temporal scale.
     * 
     * 
     * @return A similarity value between 0 and 1.
     */
    public double similarity(Fixation fixation, double dSpacial, double dTemporal) {
        long da = this.getFixationDuration();
        long db = new FixationUtil(fixation).getFixationDuration();
        
        double delta = Math.abs(da - db);
        double dist = getCenter().distance(fixation.getCenter());
        
        double dtmp = $.alg.limit(0.0, 1 - (delta / dTemporal), 1.0);
        double ddist = $.alg.limit(0.0, 1 - (dist / dSpacial), 1.0);
        
        return (dtmp + ddist) / 2;
    }
     
    

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation#getCenter()
     */
    @Override
    public Point getCenter() {
       return this.object.getCenter();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation#getTrackingEvents()
     */
    @Override
    public List<EyeTrackingEvent> getTrackingEvents() {
        return this.object.getTrackingEvents();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Fixation(" + getCenter().x + "/" + getCenter().y + ", @"  + this.trackingEvents.size() +")"; 
    }
}
