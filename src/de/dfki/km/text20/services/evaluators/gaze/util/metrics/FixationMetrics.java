package de.dfki.km.text20.services.evaluators.gaze.util.metrics;

import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Various metrics regarding fixations
 * 
 * @author Eugen Massini
 */
public class FixationMetrics {
    /**
     * Calculates the density of a fixation based on the farmost element.  
     * 
     * @param fixation
     * 
     * @return TODO.
     */
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
     * Calculates how healthy the fixation event is.
     *  
     * @param fixation 
     * 
     * @return .
     */
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
