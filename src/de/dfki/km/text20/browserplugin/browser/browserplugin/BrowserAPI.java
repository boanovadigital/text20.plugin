/*
 * BrowserAPI.java
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
package de.dfki.km.text20.browserplugin.browser.browserplugin;

import java.util.List;

/**
 * API which may be used from the JavaScript site.
 * 
 * @author Ralf Biedert
 *
 */
public interface BrowserAPI {

    /**
     * Performs a batch call to the plugin. The batch method will then call itself back a number of 
     * times with the given parameters each.
     * 
     * @param call 
     */
    public void batch(String call);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Calls a generic function
     * 
     * @param call 
     *  
     * @return . 
     */
    public Object callFunction(String call);

    /**
     * Returns the list of all known extension .
     * 
     * @return .
     */
    public List<String> getExtensions();

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Returns a previously set preference or the the 
     * default value.
     * 
     * @param key
     * @param deflt 
     * @return .
     * 
     */
    public String getPreference(String key, String deflt);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Logs a string to the Java console for debugging
     * 
     * @param toLog 
     */
    public void logString(String toLog);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * Registers a JS handler for the specified type. 
     * 
     * @param type
     * @param listener
     */
    public void registerListener(String type, String listener);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * Removes a JS handler for the specified type. 
     * 
     * @param listener
     */
    public void removeListener(String listener);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Sets a string to the preferences.
     * 
     * @param key
     * @param value
     */
    public void setPreference(String key, String value);

    /**
     * Sets a session parameter
     *  
     * @param key
     * @param value
     * 
     */
    public void setSessionParameter(String key, String value);

    /**
     * May be called from JavaScript to trigger a callback
     * 
     * @param callback
     */
    public void testBasicFunctionality(String callback);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Tells the plugin the current bounds of the document area in screen coordinates (NOTE: Do NOT use 
     * screenX and ...Y directly, as the do not tell the document area, but the outer browser frame. 
     * 
     * 
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void updateBrowserGeometry(int x, int y, int w, int h);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Updates the upper-left start position which is currently displayed in the field.
     * 
     * @param x
     * @param y
     */
    public void updateDocumentViewport(int x, int y);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * 
     * Updates a flag at a given element
     * 
     * @param id 
     * @param flag 
     * @param value 
     * 
     */
    public void updateElementFlag(String id, String flag, boolean value);

    /**
     * Updates the meta information about a given element 
     * 
     * @param id
     * @param key
     * @param value
     */
    public void updateElementMetaInformation(String id, String key, String value);

    /**
     * <b>Intended to be called from JavaScript</b><br/>
     * <br/>
     * Updates information about a HTML element .
     * 
     * 
     * @param id ID as used in the web page. Note that this is NOT the HTML-id, but can be something on its own. 
     * @param type What type the element is "text", "image", ...
     * @param content The content of the tag. If text, the text, if image the url.
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public void updateElementGeometry(String id, String type, String content, int x,
                                      int y, int w, int h);
}