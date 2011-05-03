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

import de.dfki.km.text20.browserplugin.services.pagemanager.PageManager;
import de.dfki.km.text20.services.pseudorenderer.options.GetAllElementsIntersectingOption;

/**
 * The pseudo renderer is a data structure which keeps information linked to 
 * the true rendering device (e.g., the graphics card / screen). It is used internally to 
 * keep track of 'what is going on' in the browser, as there is no way for a plugin to 
 * access the browser's internal rendering state. It contains, for example, the screen size,
 * the size and location of the browser window and the therein rendered text, images, and 
 * so forth.<br/><br/>
 * 
 * In order to work, the pseudo renderer needs help from the text20.js library which, at 
 * runtime, analyzes the page's layout and transmits these bits into the renderer (with the 
 * help of the {@link PageManager}). <br/><br/>
 * 
 * You can use the pseudo renderer to query what is where on the screen and convert coordinates. 
 *  
 * @author Ralf Biedert
 * @since 1.0
 */
public interface Pseudorenderer {
    /**
     * Given a point p in {@link CoordinatesType} 'fromType' into a point in toType coordinates. Is usually called 
     * with screen coordinates.  
     * 
     * @param p  The point to convert.
     * @param fromType Specifies relative to what the point was given.
     * @param toType The target coordinate type.
     * @return The new point, or null if it could not be converted (e.g., was not on the screen anymore).
     */
    public Point convertPoint(Point p, CoordinatesType fromType, CoordinatesType toType);

    /**
     * Creates a {@link RenderElement} contained inside the document. You should not call this method.
     * 
     * @param renderElement The type of element to create.
     * @param <T> The type of element.
     * @return The newly created element.
     */
    public <T extends RenderElement> T createElement(Class<T> renderElement);

    /**
     * Removes a {@link RenderElement} from this renderer. 
     * 
     * @param element The element to remove.
     */
    public void removeElement(RenderElement element);

    /**
     * All elements that have at least a point inside the region are returned.
     * 
     * @param rectangle The rectangle to query.
     * @param position How the coordinates should be interpreted.
     * @param options Additional options. 
     * @return A list of all elements.
     */
    public Collection<RenderElement> getAllElementsIntersecting(
                                                                Rectangle rectangle,
                                                                CoordinatesType position,
                                                                GetAllElementsIntersectingOption... options);

    /**
     * Returns the window coordinates (always in screen coordinates).
     * 
     * @return The rectangle where the window is on the screen.
     */
    public Rectangle getGeometry();

    /**
     * Returns all active render element status elements.
     * 
     * @return A list of all status elements.
     */
    public Collection<PseudorendererStatus> getStatus();

    /**
     * Returns the start of the viewport (always in document coordinates).
     * 
     * @return The current start of the viewport.
     */
    public Point getViewport();

    /**
     * Sets the window geometry of the related renderer. Only used internally. Note to self: remember, the 
     * screenX and screenY coordinates <b>*ARE NOT*</b> the JavaScirpt variables window.screenX and 
     * window.screenY! The JavaScript variables reflect the upper left corner of the outer browser 
     * frame, the geometry in our case however reflects the true rendering (document-) area.
     *   
     * @param geometry The geometry of the document area, always screen coordinates. 
     */
    public void setGeometry(Rectangle geometry);

    /**
     * Sets the status of the renderer
     * 
     * @param status The status to enable / disable.
     * @param value Either true or false.
     */
    public void setStatus(PseudorendererStatus status, boolean value);

    /**
     * Sets the start of the viewport.
     * 
     * @param startPoint The new start, always document coodinates. 
     */
    public void setViewport(Point startPoint);
}
