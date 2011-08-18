/*
 * RenderElementUtil.java
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
package de.dfki.km.text20.services.pseudorenderer.util.elements;

import java.awt.Rectangle;
import java.io.Serializable;

import net.jcores.jre.utils.VanillaUtil;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;
import de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * Utility functions for render elements.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class RenderElementUtil extends VanillaUtil<RenderElement> implements RenderElement {
    
    /**
     * Wraps a render element.
     * 
     * @param renderElement The element to wrap.
     */
    public RenderElementUtil(RenderElement renderElement) {
        super(renderElement);
    }

    /**
     * Returns a pretty name for rendering.
     * 
     * @return The pretty name.
     */
    public String getPrettyName() {
        if (this.object instanceof TextualRenderElement)
            return ((TextualRenderElement) this.object).getContent();

        if (this.object instanceof GraphicalRenderElement)
            return ((GraphicalRenderElement) this.object).getSource();

        return this.object.toString();
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getCoordinatesType()
     */
    public CoordinatesType getCoordinatesType() {
        return this.object.getCoordinatesType();
    }

    /**
     * @param type
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getGeometry(de.dfki.km.text20.services.pseudorenderer.CoordinatesType)
     */
    public Rectangle getGeometry(CoordinatesType type) {
        return this.object.getGeometry(type);
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getIdentifier()
     */
    public String getIdentifier() {
        return this.object.getIdentifier();
    }

    /**
     * @param key
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute)
     */
    public Serializable getMetaAttribute(RenderElementMetaAttribute key) {
        return this.object.getMetaAttribute(key);
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getZIndex()
     */
    public int getZIndex() {
        return this.object.getZIndex();
    }

    /**
     * @param key
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#hasMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute)
     */
    public boolean hasMetaAttribute(RenderElementMetaAttribute key) {
        return this.object.hasMetaAttribute(key);
    }

    /**
     * @return .
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#isVisible()
     */
    public boolean isVisible() {
        return this.object.isVisible();
    }

    /**
     * @param rectangle
     * @param type
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setGeometry(java.awt.Rectangle, de.dfki.km.text20.services.pseudorenderer.CoordinatesType)
     */
    public void setGeometry(Rectangle rectangle, CoordinatesType type) {
        this.object.setGeometry(rectangle, type);
    }

    /**
     * @param id
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setIdentifier(java.lang.String)
     */
    public void setIdentifier(String id) {
        this.object.setIdentifier(id);
    }

    /**
     * @param key
     * @param value
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute, java.io.Serializable)
     */
    public void setMetaAttribute(RenderElementMetaAttribute key, Serializable value) {
        this.object.setMetaAttribute(key, value);
    }

    /**
     * @param visible
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setVisible(boolean)
     */
    public void setVisible(boolean visible) {
        this.object.setVisible(visible);
    }

    /**
     * @param zindex
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setZIndex(int)
     */
    public void setZIndex(int zindex) {
        this.object.setZIndex(zindex);
    }
}
