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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.fixation;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.util.filter.displacement.MagneticDisplacementFilter;
import de.dfki.km.text20.util.filter.fixation.FixationFilter;

/**
 * @author rb
 *
 */
public class FixationHandlerOld extends AbstractGazeHandler {

    final MagneticDisplacementFilter displacementFilter = new MagneticDisplacementFilter();

    final FixationFilter filter = new FixationFilter();

    final Dimension searchSize = new Dimension(100, 200);

    /**
     * @param event
     */
    @SuppressWarnings("boxing")
    public void handleEvent(final EyeTrackingEvent event) {

        final List<String> handler = this.masterGazeHandler.getHandlerForType("fixation");
        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        // Put entry in filter
        final EyeTrackingEvent filterEvent = this.filter.filterEvent(event);

        // Update the displacement filter
        this.displacementFilter.updateDocumentOffset(this.pseudorenderer.getViewport());
        this.displacementFilter.updateScreenPosition(this.pseudorenderer.getGeometry());

        final Rectangle region = new Rectangle(event.getGazeCenter(), this.searchSize);
        region.x -= this.searchSize.width / 2;
        region.y -= this.searchSize.height / 2;

        // Update elements in region.
        final Collection<RenderElement> allElementsIntersecting = this.pseudorenderer.getAllElementsIntersecting(region, CoordinatesType.SCREEN_BASED);
        for (final RenderElement renderElement : allElementsIntersecting) {
            this.displacementFilter.updateRenderElement(renderElement);
        }

        final EyeTrackingEvent attracted = this.displacementFilter.filterEvent(filterEvent);

        Point fixation = this.filter.getFixation().getCenter();

        // If nothing changed ...
        if (!(this.filter.isNewFixation() || this.filter.isEndOfFixation())) return;

        if (fixation == null) {
            // Execute all handler
            for (final String h : handler) {
                this.browserPlugin.executeJSFunction(h, -1, -1);
            }
        } else {
            fixation = attracted.getGazeCenter();
            // Find what's below the gaze
            final Point dp = this.pseudorenderer.convertPoint(fixation, CoordinatesType.SCREEN_BASED, CoordinatesType.DOCUMENT_BASED);
            if (dp == null) return;

            // Execute all handler
            for (final String h : handler) {
                this.browserPlugin.executeJSFunction(h, dp.x, dp.y);
            }
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler#registerToEvaluator(de.dfki.km.augmentedtext.services.gazeevaluator.GazeEvaluator)
     */

    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {
        evaluator.addGazeEvaluationListener(new FixationListener() {

            public void newGazeEvaluationEvent(final FixationEvent event) {
                //
            }
        });
    }
}
