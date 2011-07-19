/*
 * FixationHandler.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.fixation.v1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import net.xeoh.plugins.base.util.OptionUtils;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationDummy;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionFixationParameters;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 *
 * @author Ralf Biedert
 */
public class FixationHandler extends AbstractGazeHandler<FixationEvent, FixationListener> {
    /** */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /** All datapoints of the current fixation*/
    final List<EyeTrackingEvent> currentFixation = new ArrayList<EyeTrackingEvent>();

    /** How many consecutive outliers we had */
    int numConsecutiveOutliers = 0;

    /** Points outside the current fixation */
    final List<EyeTrackingEvent> outliers = new ArrayList<EyeTrackingEvent>();

    /** Minimal time for a fixation to be recognized */
    private int minimalTime;

    /** Minimal time for a fixation to be recognized */
    private int radiusFixationSize;
    
    /** Minimal number of events we need */
    private int minNumberOfEvents;

    /** Used to detect event flow anomalies */
    private long lastObservedEventTime = Long.MAX_VALUE;

    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler#init()
     */
    @Override
    public void init() {
        final OptionFixationParameters defaultParameters = new OptionFixationParameters(25, 100);
        final OptionUtils<AddGazeEvaluationListenerOption> ou = new OptionUtils<AddGazeEvaluationListenerOption>(this.options);
        
        this.minimalTime = ou.get(OptionFixationParameters.class, defaultParameters).getMinimalTime();
        this.radiusFixationSize = ou.get(OptionFixationParameters.class, defaultParameters).getRadiusFixationSize();
        this.minNumberOfEvents = ou.get(OptionFixationParameters.class, defaultParameters).getMinFixationEvents();
    }

    
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler#newTrackingEvent(de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent)
     */
    @Override
    public void newTrackingEvent(final EyeTrackingEvent filteredEvent) {

        // First perform an anomaly detection
        final long eventTime = filteredEvent.getEventTime();
        final long deltaTime = eventTime - this.lastObservedEventTime;
        if (deltaTime > 1000) {
            // FIXME: Does not appear to be a general problem, investigate more closely.
            this.logger.fine("The last observed tracking event was very long ago. You should really check your tracking input!");

            // Inform about the last event
            callListener(filteredEvent, FixationEventType.FIXATION_END, this.currentFixation);

            // And reset fixation information
            this.numConsecutiveOutliers = 0;
            this.outliers.clear();
            this.currentFixation.clear();
        }

        // Update the last event time
        this.lastObservedEventTime = eventTime;

        // Now for the real processing
        final Point center = getCenter(this.currentFixation);

        // We have two lists, one containing all points which add to the current fixation, and a
        // list of point that are unrelated to the current fixation (i.e., outliers)

        // Here we check if a new point is inside or outside the current area of the fixation list
        // If it is inside (code far down below), we will simply add the new point, this is the "good"
        // case. If the point is outside a given radius, we need some logic to decide what to do.
        // Basically this can be caused by two things: A random outlier (measurement failure), or a
        // systematic gaze to another position.
        if (center.distance(filteredEvent.getGazeCenter()) > this.radiusFixationSize) {

            // In any case, increase the number of outliers and add the new "bad point"
            this.numConsecutiveOutliers++;
            this.outliers.add(filteredEvent);

            // Compute the maximal distance inside the outliers. This is a good indicator if they are all close together
            // or just randomly scattered
            final float maxOSize = getMaxCenterDistance(this.outliers);

            // If the maximal size is too large we just remove the oldest point the rejuvenate the list. If we don't do this
            // old outliers might prevent us from detecting a new fixation properly.
            if (maxOSize > this.radiusFixationSize) {
                this.outliers.remove(0);
            }

            // If the size it good enough AND we have enough evidence (in this part at least 3 points) we assume
            // we found a new fixation point.
            if (maxOSize <= this.radiusFixationSize && this.outliers.size() >= this.minNumberOfEvents && timeOf(this.outliers) >= this.minimalTime) {
                // If we already had a fixaion, call listener to end it.
                if (this.currentFixation.size() > 0) {
                    callListener(filteredEvent, FixationEventType.FIXATION_END, this.currentFixation);
                }

                // Swap lists and reset outliers count
                this.numConsecutiveOutliers = 0;
                this.currentFixation.clear();
                this.currentFixation.addAll(this.outliers);
                this.outliers.clear();

                // Inform about new fixation
                callListener(filteredEvent, FixationEventType.FIXATION_START, this.currentFixation);
            }

            // If we have a current fixation and we have more than three randomly scattered outliers this
            // fixation is is considered as finished, but without the creation of a new fixation. This could happen if, for example,
            // the user started to followed a moving target.
            if (this.numConsecutiveOutliers > this.minNumberOfEvents && this.currentFixation.size() > 0) {
                this.numConsecutiveOutliers = 0;
                this.outliers.clear();
                callListener(filteredEvent, FixationEventType.FIXATION_END, this.currentFixation);
                this.currentFixation.clear();
            }

        } else {
            // If we have a new, good point, there can't be any consecutive number of failures anymore ...
            this.numConsecutiveOutliers = 0;
            this.currentFixation.add(filteredEvent);
            callListener(filteredEvent, FixationEventType.FIXATION_CONTINUED, this.currentFixation);
        }

    }

    /**
     * Compute time of events.
     *
     * @param events
     * @return
     */
    private int timeOf(List<EyeTrackingEvent> events) {
        if (events.size() < 1) return 0;

        final long start = events.get(0).getEventTime();
        final long stop = events.get(events.size() - 1).getEventTime();
        return (int) (stop - start);
    }

    /**
     * Call listener with a new point.
     *
     * @param originalEvent
     * @param fixation
     */
    private void callListener(final EyeTrackingEvent originalEvent,
                              final FixationEventType type,
                              final List<EyeTrackingEvent> trackingEvents) {

        final Point center = getCenter(trackingEvents);
        final List<EyeTrackingEvent> myTrackingEvents = new ArrayList<EyeTrackingEvent>(trackingEvents);

        final FixationDummy fixation = new FixationDummy();
        fixation.center = (Point) center.clone();
        fixation.events = Collections.unmodifiableList(myTrackingEvents);
        
        callListener(new FixationEvent() {
            @Override
            public Fixation getFixation() {
                return fixation;
            }

            @Override
            public long getGenerationTime() {
                if (trackingEvents.size() == 0) return 0;
                return trackingEvents.get(trackingEvents.size() - 1).getEventTime();
            }

            @Override
            public FixationEventType getType() {
                return type;
            }
        });
    }

    /**
     * Returns the age of the list.
     *
     * @param events
     * @return
     */
    @SuppressWarnings("unused")
    private long getAgeOf(final List<EyeTrackingEvent> events) {
        if (events.size() <= 1) return 0;

        final EyeTrackingEvent first = events.get(0);
        final EyeTrackingEvent last = events.get(events.size() - 1);

        return last.getEventTime() - first.getEventTime();
    }

    /**
     * Returns the center of the points.
     *
     * @param events
     * @return
     */
    private Point getCenter(final List<EyeTrackingEvent> events) {

        final Point center = new Point();

        if (events == null) return null;
        if (events.size() == 0) return new Point();

        for (final EyeTrackingEvent trackingEvent : events) {
            final Point p = trackingEvent.getGazeCenter();
            center.x += p.x;
            center.y += p.y;
        }

        center.x /= events.size();
        center.y /= events.size();

        return center;
    }

    /**
     * The maximal distance of the points from their center.
     *
     * @param events
     * @return
     */
    private float getMaxCenterDistance(final List<EyeTrackingEvent> events) {

        final Point center = getCenter(events);

        float max = Float.MIN_VALUE;

        for (final EyeTrackingEvent trackingEvent : events) {
            final Point p = trackingEvent.getGazeCenter();

            max = (float) Math.max(p.distance(center), max);
        }

        return max;
    }
}
