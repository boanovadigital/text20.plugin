/*
 * PerusalHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.reading;

import java.util.List;
import java.util.logging.Logger;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalListener;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;

/**
 * Handles computed perusal events and dispatches them to the JavaScript layer.
 * 
 * @author Ralf Biedert
 */
public class PerusalHandler extends AbstractGazeHandler {
    /** */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Handles the next event
     * 
     * @param event
     */
    protected void handleEvent(final PerusalEvent event) {
        if (this.reducedCommunication) return;

        final List<String> handler = this.masterGazeHandler.getHandlerForType("perusal");
        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        this.logger.fine("Perusing with " + Double.NaN);

        // TODO
        for (final String h : handler) {
            // this.browserPlugin.executeJSFunction(h, delta, r.x, r.y, r.width, r.height,
            // "FIXME");
        }
    }

    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {

        evaluator.addEvaluationListener(new PerusalListener() {

            @Override
            public void newEvaluationEvent(PerusalEvent event) {
                 // TODO Auto-generated method stub

             }

            @SuppressWarnings("synthetic-access")
            @Override
            public Pseudorenderer getPseudorenderer() {
                return PerusalHandler.this.pseudorenderer;
            }
        });
    }
}
