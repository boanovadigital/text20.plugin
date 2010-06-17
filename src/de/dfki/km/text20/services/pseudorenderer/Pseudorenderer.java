/*
 * Pseudorenderer.java
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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;

import de.dfki.km.text20.services.pseudorenderer.options.GetAllElementsIntersectingOption;

/**
 * A pseudo-renderer is a (hidden) data structure which keeps information liked to 
 * a "true" rendering device, like it's windowGeometry and contained shapes.
 *  
 * @author rb
 */
public interface Pseudorenderer {
    /**
     * Given a point p in coordinates type 'fromType' into a point in toType coordinates. Is usually called 
     * with screen coordinates.  
     * 
     * @param p 
     * @param fromType 
     * @param toType 
     * @return The document point or null if not in document
     */
    public Point convertPoint(Point p, CoordinatesType fromType, CoordinatesType toType);

    /**
     * Creates an element contained inside the document.
     * 
     * @param renderElement 
     * @param <T> 
     * 
     * @return . 
     */
    public <T extends RenderElement> T createElement(Class<T> renderElement);

    /**
     * Removes an element from this renderer. 
     * 
     * @param element
     */
    public void removeElement(RenderElement element);

    /**
     * All elements that have at least a point inside the region are returned.
     * 
     * @param rectangle
     * @param position How the coordinates should be interpreted.
     * @param options 
     * @return .
     */
    public Collection<RenderElement> getAllElementsIntersecting(
                                                                Rectangle rectangle,
                                                                CoordinatesType position,
                                                                GetAllElementsIntersectingOption... options);

    /**
     * Returns the window coordinates.
     * 
     * @return .
     */
    public Rectangle getGeometry();

    /**
     * Returns all active render element status elements
     * 
     * @return . 
     */
    public Collection<PseudorendererStatus> getStatus();

    /**
     * Returns the start of the viewport.
     * 
     * @return . 
     */
    public Point getViewport();

    /**
     * Sets the windowGeometry of the related renderer. REMEMBER: The screenX and screenY 
     * coordinates <b>*DO NOT*</b> the JavaScirpt variables window.screenX and 
     * window.screenY!. The JavaScript variables reflect the 
     * upper left corner of the OUTER browser frame. The document however is contained 
     * @param geometry 
     * 
     */
    public void setGeometry(Rectangle geometry);

    /**
     * Sets the status of the renderer
     * 
     * @param status
     * @param value
     */
    public void setStatus(PseudorendererStatus status, boolean value);

    /**
     * Sets the start of the viewport.
     * 
     * @param startPoint 
     */
    public void setViewport(Point startPoint);
}
