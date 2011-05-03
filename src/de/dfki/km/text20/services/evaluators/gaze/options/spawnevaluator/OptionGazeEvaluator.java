/*
 * OptionGazeEvaluator.java
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
package de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator;

import de.dfki.km.text20.services.evaluators.common.options.SpawnEvaluatorOption;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;

/**
 * Specifies which {@link GazeEvaluator} to use.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class OptionGazeEvaluator implements SpawnEvaluatorOption {

    /**  */
    private static final long serialVersionUID = 358315534220091144L;

    /** */
    private final GazeEvaluator gazeEvaluator;

    /**
     * Constructs a new options object. 
     * 
     * @param gazeEvaluator The evaluator to use.
     */
    public OptionGazeEvaluator(GazeEvaluator gazeEvaluator) {
        this.gazeEvaluator = gazeEvaluator;
    }

    /**
     * Returns the evaluator.
     * 
     * @return The evaluator.
     */
    public GazeEvaluator getGazeEvaluator() {
        return this.gazeEvaluator;
    }
}
