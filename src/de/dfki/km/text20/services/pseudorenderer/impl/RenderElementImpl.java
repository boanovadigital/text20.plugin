/*
 * RenderElementImpl.java
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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;

/**
 * Implementation of the RenderElement interface
 *
 * @author rb
 */
public class RenderElementImpl implements RenderElement {

    /** Contains the meta information of this render element */
    private final Map<RenderElementMetaAttribute, Serializable> metaInformation = new HashMap<RenderElementMetaAttribute, Serializable>();

    /** Attached pseudorenderer */
    private final PseudorendererImpl pseudorenderer;

    /** Our rectangle relative to our corrdinated anchor. */
    private final Rectangle rectangle = new Rectangle();

    /** Increased with every change */
    private long changeID = 0;

    /** Our coordinates anchor, document based is default. */
    private CoordinatesType coordinatesType = CoordinatesType.DOCUMENT_BASED;

    /** ID */
    private String identifier;

    /** Which layer we're in. */
    private int zindex = 0;

    /** If we are visible or not */
    private boolean visible;

    /**
     * @param pseudorendererImpl
     */
    public RenderElementImpl(final PseudorendererImpl pseudorendererImpl) {
        this.pseudorenderer = pseudorendererImpl;
        this.metaInformation.put(RenderElementMetaAttribute.MISC, new HashMap<String, Serializable>());

        updateChangeID();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getCoordinatesType()
     */
    @Override
    public CoordinatesType getCoordinatesType() {
        return this.coordinatesType;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getGeometry(de.dfki.km.augmentedtext.services.pseudorenderer.CoordinatesType)
     */
    @Override
    public Rectangle getGeometry(final CoordinatesType ct) {

        // 1. normalize element position to document
        final Rectangle rval = (Rectangle) this.rectangle.clone();

        final Point viewport = this.pseudorenderer.getViewport();
        final Rectangle window = this.pseudorenderer.getGeometry();

        switch (this.coordinatesType) {
        case DOCUMENT_BASED:
            break;
        case VIEWPORT_BASED:
            rval.x += viewport.x;
            rval.y += viewport.y;
            break;
        case SCREEN_BASED:
            // Still does not make sense ...
            throw new NotImplementedException();
        }

        // 2. rval is now in document coordinates. Now convert to requested output format
        switch (ct) {
        case DOCUMENT_BASED:
            break;
        case VIEWPORT_BASED:
            rval.x -= viewport.x;
            rval.y -= viewport.y;
            break;
        case SCREEN_BASED:
            // FIXME: Check if elements are visible, or outside the viewport / screen
            rval.x += window.x - viewport.x;
            rval.y += window.y - viewport.y;
        }

        return rval;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setGeometry(java.awt.Rectangle, de.dfki.km.augmentedtext.services.pseudorenderer.CoordinatesType)
     */
    @Override
    public void setGeometry(final Rectangle rectangle, final CoordinatesType type) {
        updateChangeID();

        if (type == CoordinatesType.DOCUMENT_BASED || type == CoordinatesType.VIEWPORT_BASED) {
            this.rectangle.x = rectangle.x;
            this.rectangle.y = rectangle.y;
            this.rectangle.width = rectangle.width;
            this.rectangle.height = rectangle.height;
            this.coordinatesType = type;
            return;
        }

        // Setting element-positions by screen position doesn't make sense ...
        throw new NotImplementedException();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(final String id) {
        updateChangeID();
        this.identifier = id;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RenderElement Rectangle=" + this.rectangle;
    }

    /**
     * @return
     */
    protected long getChangeID() {
        return this.changeID;
    }

    /**
     * Updates our change ID.
     */
    protected void updateChangeID() {
        this.changeID = this.pseudorenderer.getChangeID();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getZIndex()
     */
    @Override
    public int getZIndex() {
        return this.zindex;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setZIndex(int)
     */
    @Override
    public void setZIndex(int zindex) {
        updateChangeID();
        this.zindex = zindex;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getMetaInformation(java.lang.String)
     */
    @Override
    public Serializable getMetaAttribute(RenderElementMetaAttribute key) {
        return this.metaInformation.get(key);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#hasMetaAttribute(java.lang.String)
     */
    @Override
    public boolean hasMetaAttribute(RenderElementMetaAttribute key) {
        return this.metaInformation.containsKey(key);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setMetaAttribute(java.lang.String, java.io.Serializable)
     */
    @Override
    public void setMetaAttribute(RenderElementMetaAttribute key, Serializable value) {
        updateChangeID();
        this.metaInformation.put(key, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#isVisible()
     */
    @Override
    public boolean isVisible() {
        return this.visible;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
