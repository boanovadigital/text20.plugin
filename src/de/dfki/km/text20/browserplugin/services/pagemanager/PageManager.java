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

/**
 * Manages the information a web page provides (about windowGeometry and events). It is a 
 * wrapper between the JavaScript side and the pseudorenderer.
 * 
 * Note: this class is not necessarily "bound" to the applet, it can be used fine without it. 
 * The only reason this class is inside the applet-services directory and not the general service 
 * directory is that the String parameter encode a few special messages which are not obvious  
 * 
 * @author rb
 */
public interface PageManager {

    /**
     * Updates the windowGeometry of the browser.
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void updateBrowserGeometry(int x, int y, int w, int h);

    /**
     * Updates the viewport. 
     * 
     * @param x
     * @param y
     */
    public void updateDocumentViewport(int x, int y);

    /**
     * 
     * @param id
     * @param flag
     * @param value
     */
    public void updateElementFlag(String id, String flag, boolean value);

    /**
     * Creates or updates an element
     * 
     * @param id
     * @param type
     * @param content
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void updateElementGeometry(String id, String type, String content, int x,
                                      int y, int w, int h);

    /**
     * @param id
     * @param key
     * @param value
     */
    public void updateElementMetaInformation(String id, String key, String value);

}