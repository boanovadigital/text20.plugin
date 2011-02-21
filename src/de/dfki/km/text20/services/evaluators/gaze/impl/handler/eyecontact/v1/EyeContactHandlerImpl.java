/*
 * RawHandlerImpl.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.eyecontact.v1;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.eyecontact.EyeContactEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.eyecontact.EyeContactEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.eyecontact.EyeContactListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventUtil;

/**
 * @author Ralf Biedert
 */
public class EyeContactHandlerImpl extends
        AbstractGazeHandler<EyeContactEvent, EyeContactListener> {

    /** Current 'mode' we are in, we assume we start without eye contact */
    EyeContactEventType currentMode = EyeContactEventType.EYECONTACT_LOST;

    /** Starting time of absence */
    long absenceTime = Long.MAX_VALUE;

    /**
     * @param listener
     * @param options
     */
    public EyeContactHandlerImpl(final EyeContactListener listener,
                                 AddGazeEvaluationListenerOption... options) {
        super(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.impl.AbstractGazeHandler#
     * requireUnfilteredEvents()
     */
    @Override
    public void newTrackingEvent(final EyeTrackingEvent event) {
        final EyeTrackingEventUtil etu = new EyeTrackingEventUtil(event);

        // In the FOUND mode we search for NO eyes>
        if (this.currentMode == EyeContactEventType.EYECONTACT_FOUND) {
            // When we have none, inform our clients
            if (etu.bothInvisible()) {
                this.currentMode = EyeContactEventType.EYECONTACT_LOST;
                this.absenceTime = event.getEventTime();
                callListener(createEvent(event, EyeContactEventType.EYECONTACT_LOST, 0));
            }

            return;
        }

        // In the LOST mode we search for eyes, any eye.
        if (this.currentMode == EyeContactEventType.EYECONTACT_LOST) {
            // When we have one, inform our clients
            if (!etu.bothInvisible()) {
                final long time = event.getEventTime() - this.absenceTime;
                this.currentMode = EyeContactEventType.EYECONTACT_FOUND;
                this.absenceTime = Long.MAX_VALUE;
                callListener(createEvent(event, EyeContactEventType.EYECONTACT_FOUND, time < 0 ? 0 : time));
            }

            // TODO: Also check blinking ... best way: check successive eye tracking
            // events for changes in pupil size ...

            return;
        }
    }

    /**
     * @param event
     * @param type
     * @param time
     * @return
     */
    EyeContactEvent createEvent(final EyeTrackingEvent event,
                                final EyeContactEventType type,
                                final long time) {
        return new EyeContactEvent() {

            @Override
            public long getGenerationTime() {
                return event.getEventTime();
            }

            @Override
            public EyeContactEventType getType() {
                return type;
            }

            @Override
            public long getAbsenceDuration() {
                return time;
            }
        };
    }
}
