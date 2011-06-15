/*
 * FixationMetrics.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.metrics;

import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Various metrics regarding fixations.
 * 
 * @author Eugen Massini
 */
@Deprecated
public class FixationMetrics {
    /**
     * Calculates the density of a fixation based on the farmost element.  
     * 
     * @param fixation The fixation to calculate the densitiy on.
     * 
     * @return The density.
     */
    @Deprecated    
    public static double calculateOpenDensity(final Fixation fixation) {
        final List<EyeTrackingEvent> trackingEvents = fixation.getTrackingEvents();
        final int lstSize = trackingEvents.size();

        double res = 0.;
        for (int i = 0; i < lstSize; ++i) {
            for (int j = i + 1; j < lstSize; ++j) {
                final EyeTrackingEvent ei = trackingEvents.get(i);
                final EyeTrackingEvent ej = trackingEvents.get(j);
                final double dist = ei.getGazeCenter().distance(ej.getGazeCenter());
                res += dist;
            }
        }
        res = res / lstSize;
        return res;

        /*
        double max = 0.0;
        List<TrackingEvent> trackingEvents = fixation.getTrackingEvents();

        for (TrackingEvent i : trackingEvents) {
            double dist = fixation.getCenter().distance(i.getGazeCenter());
            if (dist > max) max = dist;
        }

        return calculateOpenDensityWithRadius(fixation, max);
        */
    }

    /**
     * Calculates how <i>healthy</i> the fixation event is.
     *  
     * @param fixation The fixation to consider.
     * 
     * @return The healthiness value.
     */
    @Deprecated
    public static double calculateTemporalHealthiness(final Fixation fixation) {
        double baseValue = 1.0;

        final FixationUtil fixationUtil = new FixationUtil(fixation);
        final long fixationDuration = fixationUtil.getFixationDuration();

        // Improve this ...
        if (fixationDuration < 50) baseValue *= 0;
        if (fixationDuration > 400) baseValue *= 0;

        return baseValue;
    }
}
