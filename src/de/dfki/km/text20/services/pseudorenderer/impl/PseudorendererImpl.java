/*
 * PseudorendererImpl.java
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
package de.dfki.km.text20.services.pseudorenderer.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.xeoh.plugins.base.util.OptionUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;
import de.dfki.km.text20.services.pseudorenderer.options.GetAllElementsIntersectingOption;
import de.dfki.km.text20.services.pseudorenderer.options.getallelementsintersecting.OptionMagnetic;
import de.dfki.km.text20.services.pseudorenderer.options.getallelementsintersecting.OptionNoIntersections;
import de.dfki.km.text20.services.pseudorenderer.options.getallelementsintersecting.OptionOnlyValid;
import de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 *
 * @author rb
 *
 */
public class PseudorendererImpl implements Pseudorenderer {

    /** All status flags */
    private final Collection<PseudorendererStatus> allStatus = new ArrayList<PseudorendererStatus>();

    /** Where the renderer is on the screen */
    final Rectangle currentGeometry = new Rectangle();

    /** Prohibits two threads from accessing the element. */
    final Lock elementsLock = new ReentrantLock();

    /** The last change ID we emitted */
    long lastChangeID = 0;

    /** Keeps all render elements created in this context */
    final Collection<RenderElement> renderElements = new ArrayList<RenderElement>();

    /** Where the viewport starts */
    final Point viewportStart = new Point();

    /** Setup the renderer */
    public PseudorendererImpl() {
        // Make us visible
        setStatus(PseudorendererStatus.VISIBLE, true);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#convertPoint(java.awt.Point, de.dfki.km.augmentedtext.services.pseudorenderer.CoordinatesType)
     */
    @Override
    public Point convertPoint(final Point p, final CoordinatesType from, CoordinatesType to) {
        if (p == null) return null;

        final Point r = (Point) p.clone();

        if (from == CoordinatesType.SCREEN_BASED) {
            if (this.currentGeometry == null) return null;
            if (!this.currentGeometry.contains(p)) return null; //TODO: Georg: that's the point where a null centerPoint is created

            r.x -= this.currentGeometry.x;
            r.y -= this.currentGeometry.y;
            r.x += this.viewportStart.x;
            r.y += this.viewportStart.y;
        }

        if (to != CoordinatesType.DOCUMENT_BASED) throw new NotImplementedException();

        return r;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#createElement()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends RenderElement> T createElement(Class<T> renderElement) {

        RenderElement re = null;

        if (renderElement.isAssignableFrom(TextualRenderElement.class)) {
            re = new TextualRenderElementImpl(this);
        }

        if (renderElement.isAssignableFrom(GraphicalRenderElement.class)) {
            re = new GraphicalRenderElementImpl(this);
        }

        if (re == null && renderElement.isAssignableFrom(RenderElement.class)) {
            re = new RenderElementImpl(this);
        }

        // Sanity check
        if (re == null)
            throw new IllegalStateException("Ups, render element must not be null at this point");

        this.elementsLock.lock();
        try {
            this.renderElements.add(re);
        } finally {
            this.elementsLock.unlock();
        }

        // Make element visible at first
        re.setVisible(true);

        return (T) re;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#getAllElementsIntersecting(java.awt.Rectangle, de.dfki.km.augmentedtext.services.pseudorenderer.CoordinatesType, de.dfki.km.augmentedtext.services.pseudorenderer.options.GetAllElementsIntersectingOption[])
     */
    @Override
    public Collection<RenderElement> getAllElementsIntersecting(final Rectangle rectangle,
                                                                final CoordinatesType position,
                                                                final GetAllElementsIntersectingOption... options) {

        Collection<RenderElement> rval = new ArrayList<RenderElement>();

        // Handle our options ...
        final OptionUtils<GetAllElementsIntersectingOption> ou = new OptionUtils<GetAllElementsIntersectingOption>(options);

        // The point w we get is in screen coordinates. We have to calculate document coorinates first.
        final Rectangle r = (Rectangle) rectangle.clone();

        // If the coordinates are given screen-wise, convert to document position.
        if (position == CoordinatesType.SCREEN_BASED) {
            // If the gaze point is not inside the render window, do nothing ...
            if (!this.currentGeometry.intersects(rectangle)) return rval;

            r.x -= this.currentGeometry.x;
            r.y -= this.currentGeometry.y;

            r.x += this.viewportStart.x;
            r.y += this.viewportStart.y;
        }

        Collection<RenderElement> allElements;

        // Make a copy in order not to block other threads accessing the elements
        this.elementsLock.lock();
        try {
            allElements = new ArrayList<RenderElement>(this.renderElements);
        } finally {
            this.elementsLock.unlock();
        }

        // Check which rectangles intersect
        for (final RenderElement re : allElements) {
            final Rectangle geometry = re.getGeometry(CoordinatesType.DOCUMENT_BASED);
            if (geometry.intersects(r)) {
                rval.add(re);
            }
        }

        // In case we have no intersections and we're magnetic
        if (rval.size() == 0 && ou.contains(OptionMagnetic.class)) {
            final Point queryCenter = new Point((int) r.getCenterX(), (int) r.getCenterY());
            final int maxDistance = ou.get(OptionMagnetic.class).getMaxDistance();

            RenderElement bestMatch = null;
            double distance = Double.MAX_VALUE;

            // Find best match
            for (final RenderElement re : allElements) {
                final Circle current = new Circle(re.getGeometry(CoordinatesType.DOCUMENT_BASED));
                final double currentDistance = current.borderDistance(queryCenter);

                if (currentDistance < distance && currentDistance <= maxDistance) {
                    distance = currentDistance;
                    bestMatch = re;
                }
            }

            if (bestMatch != null) {
                rval.add(bestMatch);
            }
        }

        // First, filter all invalid data.
        if (ou.contains(OptionOnlyValid.class)) {
            final Collection<RenderElement> _rval = new ArrayList<RenderElement>();

            // Process elements again, only add elements which dont have an INVALID status.
            for (final RenderElement renderElement : rval) {
                if (renderElement.hasMetaAttribute(RenderElementMetaAttribute.INVALID)) {
                    if (renderElement.getMetaAttribute(RenderElementMetaAttribute.INVALID).equals(Boolean.TRUE))
                        continue;
                }

                _rval.add(renderElement);
            }

            // Replace original data
            rval = _rval;
        }

        // Next, remove intersections
        if (ou.contains(OptionNoIntersections.class))
            throw new IllegalArgumentException("Not implemented yet");

        return rval;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#getGeometry()
     */
    @Override
    public Rectangle getGeometry() {
        return (Rectangle) this.currentGeometry.clone();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#getStatus()
     */
    @Override
    public Collection<PseudorendererStatus> getStatus() {
        return this.allStatus;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#getViewport()
     */
    @Override
    public Point getViewport() {
        return (Point) this.viewportStart.clone();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#setGeometry(int, int, int, int)
     */
    @Override
    public void setGeometry(final Rectangle g) {
        this.currentGeometry.x = g.x;
        this.currentGeometry.y = g.y;
        this.currentGeometry.width = g.width;
        this.currentGeometry.height = g.height;

        // System.out.println("Updated windowGeometry: (" + screenX + "," + screenY + "," + width + "," + height + ")");
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#setStatus(de.dfki.km.augmentedtext.services.pseudorenderer.PseudorendererStatus, boolean)
     */
    @Override
    public void setStatus(final PseudorendererStatus status, final boolean value) {
        if (value == false) {
            this.allStatus.remove(status);
            return;
        }

        if (this.allStatus.contains(status)) return;

        this.allStatus.add(status);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#setViewport(int, int)
     */
    @Override
    public void setViewport(final Point start) {
        this.viewportStart.x = start.x;
        this.viewportStart.y = start.y;

        // System.out.println("Updated viewport: (" + documentX + "," + documentY + ")");
    }

    /**
     * @return
     */
    protected long getChangeID() {
        return this.lastChangeID++;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.Pseudorenderer#removeElement(de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement)
     */
    @Override
    public void removeElement(final RenderElement renderElementImpl) {

        // Hide the element
        renderElementImpl.setVisible(false);

        // Make a copy in order not to block other threads accessing the elements
        this.elementsLock.lock();
        try {

            this.renderElements.remove(renderElementImpl);
        } finally {
            this.elementsLock.unlock();
        }
    }
}
