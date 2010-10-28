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

import java.awt.Rectangle;
import java.util.List;
import java.util.logging.Logger;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalEvent;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationLineUtil;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;

/**
 *
 *
 * @author rb
 */
public class PerusalHandler extends AbstractGazeHandler {
    /** */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @SuppressWarnings("boxing")
    protected void handleEvent(final PerusalEvent event) {
        if (this.reducedCommunication) return;

        final List<String> handler = this.masterGazeHandler.getHandlerForType("perusal");
        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        for (final String h : handler) {
            final FixationLine fixationLine = event.getFixationLine();
            final FixationLineUtil fixationLineUtil = new FixationLineUtil(fixationLine);
            final Rectangle r = fixationLineUtil.getRectangle();

            System.out.println("PERUSAL LISTENER CURRENTLY DAMAGED!");

            this.browserPlugin.executeJSFunction(h, 0, r.x, r.y, r.width, r.height, "FIXME");
        }
    }

    @SuppressWarnings("boxing")
    protected void handleEventNew(final FixationLineEvent event) {
        if (this.reducedCommunication) return;

        final List<String> handler = this.masterGazeHandler.getHandlerForType("perusal");
        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        if (event.getEventType() != FixationLineEventType.FIXATION_LINE_CONTINUED)
            return;

        final FixationLine fixationLine = event.getFixationLine();
        final List<Fixation> fixations = fixationLine.getFixations();

        final int size = fixations.size();

        if (size < 2) return;

        final Fixation a = fixations.get(size - 2);
        final Fixation b = fixations.get(size - 1);

        final int x1 = a.getCenter().x;
        final int x2 = b.getCenter().x;

        final int delta = x2 - x1;

        final FixationLineUtil fixationLineUtil = new FixationLineUtil(fixationLine);
        final Rectangle r = fixationLineUtil.getRectangle();

        this.logger.fine("Perusing with " + delta);

        for (final String h : handler) {

            this.browserPlugin.executeJSFunction(h, delta, r.x, r.y, r.width, r.height, "FIXME");
        }
    }

    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {

        /*
        evaluator.addGazeEvaluationListener(new PerusalListener() {

            @SuppressWarnings("synthetic-access")
            public Pseudorenderer getPseudorenderer() {
                return PerusalHandler.this.pseudorenderer;
            }

            public void newGazeEvaluationEvent(final PerusalEvent event) {
                handleEvent(event);
            }
        });*/

        evaluator.addEvaluationListener(new FixationLineListener() {

            @Override
            public void newEvaluationEvent(FixationLineEvent event) {
                handleEventNew(event);
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public Pseudorenderer getPseudorenderer() {
                return PerusalHandler.this.pseudorenderer;
            }
        });
    }
}
