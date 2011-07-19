/*
 * BrainEvaluationListener.java
 * 
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
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
package de.dfki.km.text20.services.evaluators.brain;

import de.dfki.km.text20.services.evaluators.common.EvaluationEvent;
import de.dfki.km.text20.services.evaluators.common.EvaluationListener;

/**
 * A brain evaluation listener for given {@link BrainEvaluationEvent}s.
 * 
 * @author Ralf Biedert
 * @param <T> The type of {@link EvaluationEvent} the listener accepts.
 * @since 1.4 
 */
public interface BrainEvaluationListener<T extends BrainEvaluationEvent> extends
        EvaluationListener<T> {
    //
}
