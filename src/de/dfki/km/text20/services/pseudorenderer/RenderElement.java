/*
 * RenderElement.java
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
package de.dfki.km.text20.services.pseudorenderer;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * Represents a rendering element. Note that a render element is a 'live' object. Its values might change after you 
 * query it, depending on changes to the element or the pseudorenderer. 
 * 
 * @author Ralf Biedert
 *
 */
public interface RenderElement {

    /**
     * Returns the coordinates type this element conforms to. 
     * 
     * @return .
     */
    public CoordinatesType getCoordinatesType();

    /**
     * Returns the position of this element relative to the given type of coordinate system.
     * @param type 
     * 
     * @return .
     */
    public Rectangle getGeometry(CoordinatesType type);

    /**
     * Gets the identifier of that element which is used outside.
     *   
     * @return . 
     */
    public String getIdentifier();

    /**
     * 
     * @return .
     */
    public int getZIndex();

    /**
     * Sets the windowGeometry of this element. Be careful! The type parameter influences how these elements
     * are treated later on, it is NOT merely a coordinate transformation.
     * 
     * Elements with a DOCUMENT_POSITION don't change their position relative to the document start.  
     * Elements with a VIEWPORT_POSITION "move" their document position when the window scrolls. 
     * 
     * @param rectangle 
     * @param type 
     * 
     */
    public void setGeometry(Rectangle rectangle, CoordinatesType type);

    /**
     * Sets the identifier of that element which is used outside.
     * 
     * @param id
     */
    public void setIdentifier(String id);

    /**
     * Sets a z index.
     * 
     * @param zindex
     */
    public void setZIndex(int zindex);

    /**
     * Sets a meta attribute.
     * 
     * @param key
     * @param value
     */
    public void setMetaAttribute(RenderElementMetaAttribute key, Serializable value);

    /**
     * @param key
     * 
     * @return . 
     */
    public boolean hasMetaAttribute(RenderElementMetaAttribute key);

    /**
     * @param key
     * @return .
     */
    public Serializable getMetaAttribute(RenderElementMetaAttribute key);

    /**
     * Returns true if the element is visible.
     * 
     * @return .
     */
    public boolean isVisible();

    /**
     * 
     * @param visible
     */
    public void setVisible(boolean visible);
}
