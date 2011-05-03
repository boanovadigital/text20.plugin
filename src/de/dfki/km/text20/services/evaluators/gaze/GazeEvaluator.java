/*
 * GazeEvaluator.java
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
package de.dfki.km.text20.services.evaluators.gaze;

import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.GazeEvaluatorItem;
import de.dfki.km.text20.services.evaluators.common.Evaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;

/**
 * A gaze evaluator that evaluates raw gaze data and provides {@link GazeEvaluationEvent}s. An evaluator can be
 * obtained either by the {@link GazeEvaluatorManager} (when in library mode), or by the {@link GazeEvaluatorItem} 
 * (when in extension mode, see the {@link BrowserAPI} for details). <br/><br/>
 * 
 * Currently, the following evaluators are supported:
 * 
 * <ul>
 * <li>{@link FixationListener} - Registers a listener that will be called upon new {@link FixationEvent}s.</li>
 * </ul>
 * 
 * @author Ralf Biedert
 * @since 1.3
 */
public interface GazeEvaluator
        extends
        Evaluator<GazeEvaluationListener<? extends GazeEvaluationEvent>, AddGazeEvaluationListenerOption, GazeFilter> {
    //
}
