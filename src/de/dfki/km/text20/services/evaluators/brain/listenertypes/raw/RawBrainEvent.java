/*
 * RawDataEvent.java
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
package de.dfki.km.text20.services.evaluators.brain.listenertypes.raw;

import de.dfki.km.text20.services.evaluators.brain.BrainEvaluationEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

/**
 * A raw data event, either filtered or unfiltered (see the {@link RawBrainListener}).
 * 
 * @author Ralf Biedert
 * @since 1.4
 * @see RawBrainListener
 */
public interface RawBrainEvent extends BrainEvaluationEvent {
    /**
     * Returns the associated tracking event.
     * 
     * @return The tracking event.
     */
    public BrainTrackingEvent getTrackingEvent();
}
