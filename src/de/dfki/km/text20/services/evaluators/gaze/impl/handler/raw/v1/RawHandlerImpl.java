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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.raw.v1;

import java.util.ArrayList;
import java.util.Collection;

import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFlags;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeListener;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Ralf Biedert
 */
public class RawHandlerImpl extends AbstractGazeHandler<RawGazeEvent, RawGazeListener> {

    /** If we need unfiltered data */
    boolean requireUnfiltered;

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler#init()
     */
    @Override
    public void init() {
        this.requireUnfiltered = this.attachedListener.requireUnfilteredEvents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.impl.AbstractGazeHandler#requireUnfilteredEvents()
     */

    @Override
    public void newTrackingEvent(final EyeTrackingEvent event) {

        callListener(new RawGazeEvent() {
            @Override
            public long getGenerationTime() {
                return event.getObservationTime();
            }

            @Override
            public EyeTrackingEvent getTrackingEvent() {
                return event;
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.impl.AbstractGazeHandler#newTrackingEvent(de.dfki.km.
     * augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public Collection<GazeHandlerFlags> getFlags() {
        final ArrayList<GazeHandlerFlags> rval = new ArrayList<GazeHandlerFlags>();

        rval.add(GazeHandlerFlags.REQUIRE_RAW);

        if (this.requireUnfiltered) {
            rval.add(GazeHandlerFlags.REQUIRE_UNFILTERED);
        }

        return rval;
    }

}
