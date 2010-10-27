/*
 * ReadingEvent.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.reading;

import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationEvent;

/**
 * 
 * @author rb
 */
@Deprecated
public interface ReadingEvent extends GazeEvaluationEvent {
    // Wird gelesen oder kreuzundquer geschaut?
    // Wenn gelesen wird, wie schnell wird gelesen, oder wird überflogen?
    // Gibt es Leseschwierigkeiten?
    // Muss das alles in eine Klasse?

}
