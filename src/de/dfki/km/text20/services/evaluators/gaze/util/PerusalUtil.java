/*
 * PerusalUtil.java
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
package de.dfki.km.text20.services.evaluators.gaze.util;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalEvent;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;
import de.dfki.km.text20.services.pseudorenderer.util.TextualRenderElementCharPositions;

/**
 * Various functions on the PerusalEvent object.>
 * 
 * 
 * @author rb
 */
public class PerusalUtil {
    private List<Fixation> correctedFixations;

    /** */
    @SuppressWarnings("unused")
    private PerusalEvent perusalEvent;

    /** */
    @SuppressWarnings("unused")
    private Rectangle perusedArea;

    /** */
    private Collection<RenderElement> perusedRenderElements;

    /** */
    private Pseudorenderer pseudorenderer;

    /** */
    private String text = null;

    /**
     * @param perusalEvent
     * @param pseudorenderer 
     */
    public PerusalUtil(final PerusalEvent perusalEvent,
                       final Pseudorenderer pseudorenderer) {
        this.perusalEvent = perusalEvent;
        this.pseudorenderer = pseudorenderer;

        //this.perusedArea = perusalEvent.getPerusedArea();
        //this.perusedRenderElements = perusalEvent.getPerusedRenderElements();
        //this.correctedFixations = perusalEvent.getCorrectedFixations();

    }

    /**
     * @return .
     */
    public RenderElement getLastFixatedElement() {
        final Fixation fixation = this.correctedFixations.get(this.correctedFixations.size() - 1);
        @SuppressWarnings("unused")
        final Point location2 = this.pseudorenderer.convertPoint(fixation.getCenter(), CoordinatesType.SCREEN_BASED, CoordinatesType.DOCUMENT_BASED);

        return null;
    }

    /**
     * @return .
     */
    public int getLastFixatedWordPos() {
        final Fixation fixation = this.correctedFixations.get(this.correctedFixations.size() - 1);
        final Point location2 = this.pseudorenderer.convertPoint(fixation.getCenter(), CoordinatesType.SCREEN_BASED, CoordinatesType.DOCUMENT_BASED);

        final RenderElement element = getLastFixatedElement();

        // Get fixated word.
        if (element != null && element instanceof TextualRenderElement) {

            final TextualRenderElementCharPositions recp = new TextualRenderElementCharPositions();
            return recp.getPositionOf((TextualRenderElement) element, CoordinatesType.DOCUMENT_BASED, location2);
        }

        return -1;
    }

    /**
     * Returns the text perused since the last event. 
     * 
     * @return .
     */
    public String getPerusedText() {
        if (this.text != null) return this.text;

        this.text = "";

        // Assemble text of area.
        for (final RenderElement renderElement : this.perusedRenderElements) {
            if (renderElement instanceof TextualRenderElement) {
                this.text += ((TextualRenderElement) renderElement).getContent();
            }
        }

        return this.text;
    }

}
