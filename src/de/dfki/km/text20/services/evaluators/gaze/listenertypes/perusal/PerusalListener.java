/*
 * PerusalListener.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal;

import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Registers a perusal listener that receives {@link PerusalEvent}s. 
 * 
 * @author Ralf Biedert
 * @since 1.5
 */
public interface PerusalListener extends GazeEvaluationListener<PerusalEvent> {
    /**
     * Optional: Listeners can provide a {@link Pseudorenderer} which is then used 
     * to match the read passages with the underlying text. 
     * 
     * @return The current pseudorenderer, may be null.
     */
    public Pseudorenderer getPseudorenderer();

}
