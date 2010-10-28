/*
 * MagneticDisplacementFilter.java
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
package de.dfki.km.text20.util.filter.displacement;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.wrapper.TrackingEventWrapper;
import de.dfki.km.text20.util.filter.AbstractFilter;
import de.dfki.km.text20.util.filter.displacement.GridEntry.GridStatus;

/**
 * Displacement filter, alters the central position based on document information 
 *  
 * @author rb
 */
public class MagneticDisplacementFilter extends AbstractFilter {

    /** offset of the document in the window */
    Point documentOffset = new Point();

    /** Manges the displacement grid */
    final Grid grid = new Grid(100, 100);

    /** Ensures every RE exists only once */
    Map<String, RenderElement> renderElements = new HashMap<String, RenderElement>();

    /** Where the elements are located */
    Rectangle screenPosition = new Rectangle();

    @Override
    public EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {

        // Check if the gaze is inside the document.
        final Point documentPoint = screenToDocumentPoint(event.getGazeCenter());
        if (documentPoint == null) return event;

        // Displacement to apply to the center point
        final Point displacement = new Point();

        // Obtain grid entry for point
        final Point gazeCenter = event.getGazeCenter();
        final GridEntry gridForPoint = this.grid.getGridForPoint(gazeCenter);

        // If we already have a status for the point, return it ... (may not be the best choice ...?)
        // if (gridForPoint.gridStatus.equals(GridStatus.SET)) { return createWrapped(event, gridForPoint.xdisplacement, gridForPoint.ydisplacement); }

        // Get elements near the document gaze point
        final RenderElement bestNear = getBestElementNear(documentPoint);
        if (bestNear == null) return event;

        // Update the displacement
        displacement.y = (int) (bestNear.getGeometry(CoordinatesType.DOCUMENT_BASED).getCenterY() - documentPoint.y);
        // displacement.x = (int) (bestNear.getGeometry().getCenterX() - documentPoint.x);

        // Only return new event if we have a displacement.
        if (displacement.x > 0 || displacement.y > 0) {
            // Update grid for point information
            gridForPoint.xdisplacement = displacement.x;
            gridForPoint.ydisplacement = displacement.y;
            gridForPoint.gridStatus = GridStatus.SET;

            return createWrapped(event, displacement.x, displacement.y);
        }

        return event;
    }

    /**
     * Change document offset
     * 
     * @param o
     */
    public void updateDocumentOffset(final Point o) {
        this.documentOffset = o;
    }

    /**
     * Updates a render element inside the filter. 
     * 
     * @param re
     */
    public void updateRenderElement(final RenderElement re) {
        if (re == null) return;

        this.renderElements.put(re.getIdentifier(), re);
    }

    /**
     * Updates the screen position
     * 
     * @param r
     */
    public void updateScreenPosition(final Rectangle r) {
        this.screenPosition = r;
    }

    /**
     * Creates a wrapped tracking event
     * 
     * @param event
     * @param dx
     * @param dy
     * @return
     */
    private EyeTrackingEvent createWrapped(final EyeTrackingEvent event, final int dx,
                                           final int dy) {
        return new TrackingEventWrapper(event) {

            @Override
            public Point getGazeCenter() {
                final Point gazeCenter = event.getGazeCenter();
                return new Point(gazeCenter.x + dx, gazeCenter.y + dy);
            }
        };

    }

    /**
     * Returns the 'best'  element close to the gaze point
     *  
     * @param documentPoint
     * @return
     */
    protected RenderElement getBestElementNear(final Point documentPoint) {
        final Set<String> ids = this.renderElements.keySet();

        double distance = Double.MAX_VALUE;
        RenderElement bestGuess = null;

        for (final String id : ids) {
            final RenderElement renderElement = this.renderElements.get(id);

            // Currently we only care for text elements
            if (!(renderElement instanceof TextualRenderElement)) {
                continue;
            }

            final Rectangle geometry = renderElement.getGeometry(CoordinatesType.DOCUMENT_BASED);

            // Elements too large dont give enough information for interesting points ... We alo
            // dont care about the width, as we currently want to displace y-wise only ...
            // TODO: A bit harsh this limit ...
            if (geometry.height > 30) {
                continue;
            }

            // Use point directly above or below gaze.
            final Point p = new Point(documentPoint.x, (int) geometry.getCenterY());
            final double d = documentPoint.distance(p);

            // If the distance is too large, ignore
            // TODO: Check this limit as well
            if (d > 100) {
                continue;
            }

            if (d < distance) {
                distance = d;
                bestGuess = renderElement;
            }

        }

        return bestGuess;
    }

    /**
     * 
     * @param p
     * @return
     */
    Point screenToDocumentPoint(final Point p) {
        if (!this.screenPosition.contains(p)) return null;

        final Point rval = (Point) p.clone();
        rval.x -= this.screenPosition.x;
        rval.y -= this.screenPosition.y;
        rval.x += this.documentOffset.x;
        rval.y += this.documentOffset.y;
        return rval;
    }
}
