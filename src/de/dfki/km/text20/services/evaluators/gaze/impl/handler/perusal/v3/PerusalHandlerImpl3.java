/*
 * PerusalHandlerImpl3.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.perusal.v3;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal.PerusalListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationLineUtil;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.options.getallelementsintersecting.OptionMagnetic;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;
import de.dfki.km.text20.services.pseudorenderer.util.TextualRenderElementCharPositions;

/**
 * Detects perusal progress.
 *
 * TODO: Hard coded values MUST be replaced by sensible ones.
 *
 * TODO: Replace this with latest version from experimental branch.
 *
 * @author Ralf Biedert
 */
public class PerusalHandlerImpl3 extends
        AbstractGazeHandler<PerusalEvent, PerusalListener> {

    /** */
    Pseudorenderer pseudorenderer;

    /** */
    long currentTime;

    /** */
    TextualRenderElementCharPositions tecp = new TextualRenderElementCharPositions();

    /**
     * @param listener
     * @param options
     */
    public PerusalHandlerImpl3(final PerusalListener listener,
                               AddGazeEvaluationListenerOption... options) {
        super(listener);
    }

    @Override
    public void init(AddGazeEvaluationListenerOption... options) {

        this.pseudorenderer = this.attachedListener.getPseudorenderer();

        this.gazeEvaluator.addEvaluationListener(new FixationLineListener() {

            @Override
            public void newEvaluationEvent(final FixationLineEvent event) {
                PerusalHandlerImpl3.this.currentTime = event.getGenerationTime();
                newFixationLineEvent(event);
            }

            @Override
            public Pseudorenderer getPseudorenderer() {
                return PerusalHandlerImpl3.this.pseudorenderer;
            }
        }, options);

    }

    /**
     * Dispatches the new event to the listener.
     *
     * @param rectangle
     * @param elementsForDocumentArea
     * @param flu
     * @param word
     * @param positionOf
     * @param type
     */
    private void dispatch(final float speed, final Rectangle rectangle,
                          final FixationLine fl,
                          final Collection<RenderElement> elementsForDocumentArea) {

        // 'Exports'
        final long time = this.currentTime;

        callListener(new PerusalEvent() {

            /** */
            @SuppressWarnings("unused")
            public List<Fixation> getCorrectedFixations() {
                return fl.getFixations();
            }

            /** */
            @Override
            public long getGenerationTime() {
                return time;
            }

            /** */
            @SuppressWarnings("unused")
            public float getCharactersSkipped() {
                return speed;
            }

            @SuppressWarnings("unused")
            public Rectangle getPerusedArea() {
                return (Rectangle) rectangle.clone();
            }

            @SuppressWarnings("unused")
            public Collection<RenderElement> getPerusedRenderElements() {
                return elementsForDocumentArea;
            }

            @Override
            public FixationLine getFixationLine() {
                return fl;
            }
        });
    }

    /**
     * Calculate perusal information based on the last two fixations of the line
     * @param flu
     *
     * @param a
     * @param b
     */
    @SuppressWarnings({ "unused", "null" })
    private void dispatchWith(final FixationLine fl, final Fixation a, final Fixation b) {

        final List<Fixation> lastFixations = new ArrayList<Fixation>();
        lastFixations.add(a);
        lastFixations.add(b);

        final Point p1 = lastFixations.get(0).getCenter();
        final Point p2 = lastFixations.get(1).getCenter();

        Float characters = null;

        // Try to obtain the speed in characters per second
        if (this.pseudorenderer != null) {
            Collection<RenderElement> allElementsIntersecting;

            RenderElement r1 = null;
            RenderElement r2 = null;

            // Get elements at fixation point
            allElementsIntersecting = this.pseudorenderer.getAllElementsIntersecting(new Rectangle(p1, new Dimension(1, 1)), CoordinatesType.SCREEN_BASED, new OptionMagnetic());
            for (RenderElement renderElement : allElementsIntersecting) {
                r1 = renderElement;
            }

            allElementsIntersecting = this.pseudorenderer.getAllElementsIntersecting(new Rectangle(p2, new Dimension(1, 1)), CoordinatesType.SCREEN_BASED, new OptionMagnetic());
            for (RenderElement renderElement : allElementsIntersecting) {
                r2 = renderElement;
            }

            // Check if both are textual elements
            if (r1 != null && r1 instanceof TextualRenderElement && r2 != null && r2 instanceof TextualRenderElement) {
                TextualRenderElement t1 = (TextualRenderElement) r1;
                TextualRenderElement t2 = (TextualRenderElement) r2;

                int tID1 = t1.getTextID();
                int tID2 = t2.getTextID();

                if (tID1 == tID2) {
                    //System.out.println(t1.getWordID() + " --> " + t2.getWordID());
                    //System.out.println(t1.getContent() + " ---> " + t2.getContent());

                    int pp1 = this.tecp.getPositionOf(t1, CoordinatesType.SCREEN_BASED, p1);
                    int pp2 = this.tecp.getPositionOf(t2, CoordinatesType.SCREEN_BASED, p2);
                    //System.out.println(pp1 + " >>> " + pp2);

                }
            }
        }

        // In this case we have to guess ...
        if (characters == null) {
            // Get default font size

            // Calculate approx. number of characters based on horizontal distance
        }

        // Big, fat TODO: Adjust this based on the actual speed.
        float readSpeed = (p2.x - p1.x) / 100.0f;

        final FixationsUtil fu = new FixationsUtil(lastFixations);
        final Rectangle rectangle = fu.getRectangle();
        final Collection<RenderElement> elementsForDocumentArea = new ArrayList<RenderElement>();

        // Might be null in some cases
        if (rectangle != null) {
            final Point location = this.pseudorenderer.convertPoint(rectangle.getLocation(), CoordinatesType.SCREEN_BASED, CoordinatesType.DOCUMENT_BASED);

            if (location != null) {
                rectangle.setLocation(location);
            }
        }

        dispatch(readSpeed, rectangle, fl, elementsForDocumentArea);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.impl.AbstractGazeHandler#init()
     */

    void newFixationLineEvent(final FixationLineEvent event) {
        // DISABLED. Not a good idea. More low level layers don't know anything about
        // this and continue to send events, e.g., the FixationLineHandler. If we skip some
        // we'll miss events, e.g., the start or end of a line.

        // If we have a renderer, only do something if the window is visible.
        // if (this.pseudorenderer != null) {
        //    if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE))
        //        return;
        // }

        final FixationLine fl = event.getFixationLine();
        final FixationLineUtil flu = new FixationLineUtil(fl);

        //
        // If the fixation line was started, pick an appropriate line of text
        //
        if (event.getEventType() == FixationLineEventType.FIXATION_LINE_STARTED) {
            //
        }

        //
        // In case the line was continued
        //
        if (event.getEventType() == FixationLineEventType.FIXATION_LINE_CONTINUED) {
            //
        }

        //
        // In case the line was continued
        //
        if (event.getEventType() == FixationLineEventType.FIXATION_LINE_ENDED) {
            //
        }

        final List<Fixation> lastFixations = flu.getLastFixations(2);

        final Fixation a = lastFixations.get(0);
        final Fixation b = lastFixations.get(1);

        dispatchWith(fl, a, b);
    }

}
