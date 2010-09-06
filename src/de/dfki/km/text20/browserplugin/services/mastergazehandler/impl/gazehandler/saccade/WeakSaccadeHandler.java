/*
 * RawGazeHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.saccade;

import java.util.List;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.SaccadeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.WeakSaccadeListener;
import de.dfki.km.text20.services.evaluators.gaze.util.SaccadeUtil;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;

/**
 * 
 * 
 * @author rb
 */
public class WeakSaccadeHandler extends AbstractGazeHandler {

    /**
     * @param event
     */
    @SuppressWarnings("boxing")
    protected void handleEvent(final SaccadeEvent event) {
        if (this.reducedCommunication) return;

        final List<String> handler = this.masterGazeHandler.getHandlerForType("weakSaccade");

        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        final SaccadeUtil su = new SaccadeUtil(event.getSaccade());

        for (final String h : handler) {
            this.browserPlugin.executeJSFunction(h, su.getAngle(), su.getLength());
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler#registerToEvaluator(de.dfki.km.augmentedtext.services.gazeevaluator.GazeEvaluator)
     */
    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {

        evaluator.addEvaluationListener(new WeakSaccadeListener() {

            public void newEvaluationEvent(SaccadeEvent event) {
                handleEvent(event);
            }
        });
    }
}
