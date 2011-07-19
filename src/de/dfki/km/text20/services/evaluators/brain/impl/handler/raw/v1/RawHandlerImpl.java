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
package de.dfki.km.text20.services.evaluators.brain.impl.handler.raw.v1;

import static net.jcores.jre.CoreKeeper.$;

import java.util.Collection;
import java.util.List;

import de.dfki.km.text20.services.evaluators.brain.BrainHandlerFlags;
import de.dfki.km.text20.services.evaluators.brain.listenertypes.raw.RawBrainEvent;
import de.dfki.km.text20.services.evaluators.brain.listenertypes.raw.RawBrainListener;
import de.dfki.km.text20.services.evaluators.brain.util.handler.AbstractBrainHandler;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

/**
 * @author Ralf Biedert
 */
public class RawHandlerImpl extends AbstractBrainHandler<RawBrainEvent, RawBrainListener> {

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
    public void newTrackingEvent(final BrainTrackingEvent event) {
        callListener(new RawBrainEvent() {
            @Override
            public long getGenerationTime() {
                return event.getEventTime();
            }

            @Override
            public BrainTrackingEvent getTrackingEvent() {
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
    public Collection<BrainHandlerFlags> getFlags() {
        final List<BrainHandlerFlags> rval = $.list();
        rval.add(BrainHandlerFlags.REQUIRE_RAW);
        if (this.requireUnfiltered) {
            rval.add(BrainHandlerFlags.REQUIRE_UNFILTERED);
        }
        return rval;
    }

}
