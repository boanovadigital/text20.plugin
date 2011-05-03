/*
 * PageManager.java
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
package de.dfki.km.text20.browserplugin.services.pagemanager;

import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Handles the information the web page provides (e.g., window geometry and elements). It is 
 * mostly an adapter from the JavaScript side to the {@link Pseudorenderer} and helps it
 * to maintain a consitent state (with respect to the web page). In most cases, you 
 * won't need to bother about this object.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface PageManager {

    /**
     * Updates the window geometry of the browser (with respect to the screen).
     * 
     * @param x X position of the browser (in screen coordinates).
     * @param y Y position of the browser (in screen coordinates).
     * @param w Width of the browser.
     * @param h Height of the browser.    
     */
    public void updateBrowserGeometry(int x, int y, int w, int h);

    /**
     * Updates the document start position. 
     * 
     * @param x X position of the viewport (in document coordinates).
     * @param y Y position of the viewport (in document coordinates).
     */
    public void updateDocumentViewport(int x, int y);

    /**
     * Updates an element flag.
     *  
     * @param id The ID of the element to update.
     * @param flag The flag to update.
     * @param value The value to update.
     */
    public void updateElementFlag(String id, String flag, boolean value);

    /**
     * Creates or updates an element.
     * 
     * @param id ID as used in the web page. Note that this is NOT the HTML-id, but can be something on its own. 
     * @param type What type the element is "text", "image", ...
     * @param content The content of the tag. If text, the text, if image the url.
     * @param x X position of the element (in document coordinates).
     * @param y Y position of the element (in document coordinates).
     * @param w Width of the element.
     * @param h Height of the element.
     */
    public void updateElementGeometry(String id, String type, String content, int x,
                                      int y, int w, int h);

    /**
     * Updates an element's meta information. 
     * 
     * @param id The ID of the element to update.
     * @param key The key to set.
     * @param value The value to set.
     */
    public void updateElementMetaInformation(String id, String key, String value);

}