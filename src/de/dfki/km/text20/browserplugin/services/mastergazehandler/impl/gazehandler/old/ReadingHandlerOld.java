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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.old;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.util.filter.displacement.MagneticDisplacementFilter;
import de.dfki.km.text20.util.filter.fixation.FixationFilter;

/**
 * @author rb
 *
 */
public class ReadingHandlerOld extends AbstractGazeHandler {

    static class Event {
        public final long date;
        public final Point fixation;

        public Event(final long date, final Point fixation) {
            this.date = date;
            this.fixation = fixation;
        }
    }

    /** Used to snap to the nearest word */
    final MagneticDisplacementFilter displacementFilter = new MagneticDisplacementFilter();

    /** Calculates fixations */
    final FixationFilter fixationFilter = new FixationFilter();

    /** */
    final List<Event> lastFixations = new ArrayList<Event>();

    /**
     * Returns the first textual element at the given position, or null if there is no such. 
     * 
     * @param p
     * @return  
     */
    @SuppressWarnings("unused")
    private RenderElement getTextualElementAt(final Point p) {
        // Get element at gaze point
        final Collection<RenderElement> elementsAt = this.pseudorenderer.getAllElementsIntersecting(new Rectangle(p, new Dimension(1, 1)), CoordinatesType.SCREEN_BASED);
        for (final RenderElement renderElement : elementsAt) {
            // Check that the element is visible
            if (!renderElement.isVisible()) {
                continue;
            }

            // We're only interested in text types.
            if (!(renderElement instanceof TextualRenderElement)) {
                continue;
            }

            return renderElement;
        }

        return null;
    }

    private void updateFilter(final EyeTrackingEvent event) {
        // Update the displacement filter
        this.displacementFilter.updateDocumentOffset(this.pseudorenderer.getViewport());
        this.displacementFilter.updateScreenPosition(this.pseudorenderer.getGeometry());

        // Obtain elements arround gaze position
        final Rectangle region = new Rectangle(event.getGazeCenter(), new Dimension(100, 200));
        region.x -= 50;
        region.y -= 100;

        // Update elements in region.
        final Collection<RenderElement> allElementsIntersecting = this.pseudorenderer.getAllElementsIntersecting(region, CoordinatesType.SCREEN_BASED);

        for (final RenderElement renderElement : allElementsIntersecting) {
            this.displacementFilter.updateRenderElement(renderElement);
        }

        return;
    }

    private void updateFixations(final long date, final Point fixation) {
        this.lastFixations.add(new Event(date, fixation));

        // Need a certain minimum size
        if (this.lastFixations.size() < 2) return;

        // Obtain angle and distance of last saccade

        // Truncate size of fixations
        if (this.lastFixations.size() > 10) {
            this.lastFixations.remove(0);
        }
    }

    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {
        // TODO Auto-generated method stub

    }

    void handleEvent(final EyeTrackingEvent event) {

        final List<String> handler = this.masterGazeHandler.getHandlerForType("reading");
        if (handler.size() == 0) return;

        // If the element is no000000t visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
            return;

        // a) Put entry in filter 
        final EyeTrackingEvent filterEvent = this.fixationFilter.filterEvent(event);

        // b) Update filters with data near the current gaze point
        updateFilter(event);

        // c) And calculate the attracted position using updated data
        //final TrackingEvent attracted = this.displacementFilter.filterEvent(filterEvent);
        final EyeTrackingEvent attracted = filterEvent;

        // We're only interested in new fixations
        if (!this.fixationFilter.isNewFixation()) return;

        // Now we hopefully have better fixation information 
        final Point fixation = attracted.getGazeCenter();

        updateFixations(filterEvent.getEventTime(), fixation);
    }
}
