/*
 * FixationEvent.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation;

import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationEvent;

/**
 * A fixation event is being emitted to the {@link FixationListener} when the 
 * corresponding evaluator detected either a fixation start, a fixation 
 * continuation or a fixation end. <br/><br/>
 * 
 * In most cases you should filter the event based on the {@link FixationEventType},
 * otherwise your code will execute multiple times for the same fixation.   
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface FixationEvent extends GazeEvaluationEvent {
    /**
     * Returns the fixation related to this event.
     * 
     * @return The fixation object.
     */
    public Fixation getFixation();

    /**
     * The type of this event.
     * 
     * @return The type.
     */
    public FixationEventType getType();
}
