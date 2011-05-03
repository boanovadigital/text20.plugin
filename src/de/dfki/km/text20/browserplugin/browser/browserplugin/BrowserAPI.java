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

import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.InformationItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.GazeEvaluatorItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.JavaScriptExecutorItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.MasterGazeHandlerItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.PseudorendererItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.SessionRecorderItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * The BrowserAPI contains all primary functions that can be called from the 
 * JavaScript side. Usually there should not be the need to access any of 
 * these methods directly.<br/><br/>
 * 
 * If you are writing an {@link Extension} (<i>extension mode</i>), however, you might be interested in the exported 
 * {@link InformationItem}s of this plugin. These items can be obtained through the 
 * {@link InformationBroker} (see <a href="http://jspf.xeoh.net">the JSPF documentation for details</a>). 
 * The most commonly needed items for writing extensions are:
 * 
 * <ul>
 * <li> {@link GazeEvaluatorItem} - Use the {@link GazeEvaluator} to register <i>gaze evaluators</i> (for fixations, saccades, ...)</li>
 * <li> {@link JavaScriptExecutorItem} - Allows you to call a function in JavaScript.</li>
 * <li> {@link MasterGazeHandlerItem} - The {@link MasterGazeHandler} contains the master filter chain and some other functions.</li>
 * <li> {@link PseudorendererItem} - The {@link Pseudorenderer} keeps an internal representation 
 * of the browser's geometry, can be used to convert points from the screen to the document coordinate system (and vice versa), and many things more.</li>
 * <li> {@link SessionRecorderItem} - Allows you to access the {@link SessionRecorder} and thus the current session recording facilities.</li>
 * </ul>
 * 
 * In general, the class implementing the BrowserAPI is the heart of the plugin when running as an extension. <br/><br/>
 * 
 * 
 * However, when you are using the framework as a <b>library</b> (<i>library mode</i>), the BrowserAPI won't be instantiated and none of 
 * the above items will be available. In that case, you are responsible for properly setting up the required 
 * infrastructure (takes only a few lines of code in the basic setup, see <a href="http://code.google.com/p/text20/wiki/UsingJavaInterface">how to use 
 * the Java interface as a library</a>).
 * 
 *     
 * @author Ralf Biedert
 * @since 1.0
 */
public interface BrowserAPI {

    /**
     * Performs a batch call to the plugin. The batch method will then call itself back a number of 
     * times with the given parameters each.
     * 
     * @param call The batch call. See the JavaScript code how batch calls are being assembled.
     */
    public void batch(String call);

    /**
     * Calls a function from one of the registered extensions. 
     * 
     * @param call The call to an extension function.
     *  
     * @return . 
     */
    public Object callFunction(String call);

    /**
     * Returns the list of all known extensions.
     * 
     * @return List with all known function names.
     */
    public List<String> getExtensions();

    /**
     * Returns a previously set preference or the the 
     * default value.
     * 
     * @param key The key to get. 
     * @param deflt The default to return if nothing was found.
     * @return Either the value, or <code>deflt</code> if nothing was found.
     * 
     */
    public String getPreference(String key, String deflt);

    /**
     * Logs a string to the Java console for debugging. Deprecated, use JavaScript internal logging.
     * 
     * @param toLog The string to log.
     */
    @Deprecated
    public void logString(String toLog);

    /**
     * Registers a JavaScript handler for the specified type. 
     * 
     * @param type The type to register.
     * @param listener The function name on the JavaScript side.
     */
    public void registerListener(String type, String listener);

    /**
     * Removes a JavaScript handler for the specified type. 
     * 
     * @param listener The name of the listener to remove.
     */
    public void removeListener(String listener);

    /**
     * Sets a string in the persistent the preferences. Preferences will be stored permanently.
     * 
     * @param key The key to set.
     * @param value The value to set.
     */
    public void setPreference(String key, String value);

    /**
     * Sets a session parameter for the current session. Will only be kept in the current session.
     *  
     * @param key The key to set.
     * @param value The value to set.
     * 
     */
    public void setSessionParameter(String key, String value);

    /**
     * May be called from JavaScript to trigger a callback.
     * 
     * @param callback Callback to call.
     */
    @Deprecated
    public void testBasicFunctionality(String callback);

    /**
     * Tells the plugin the current bounds of the document area in screen coordinates (NOTE: Do NOT use 
     * screenX and ...Y directly, as the do not tell the document area, but the outer browser frame). 
     * 
     * 
     * @param x X position of the window.
     * @param y Y position of the window.
     * @param w The width.
     * @param h The height.
     */
    public void updateBrowserGeometry(int x, int y, int w, int h);

    /**
     * Updates the upper-left start position which is currently displayed in the field.
     * 
     * @param x Offset left.
     * @param y Offset top.
     */
    public void updateDocumentViewport(int x, int y);

    /**
     * Updates a flag at a given element.
     * 
     * @param id The ID of the element to update.
     * @param flag The flag to update.
     * @param value The value to update.
     * 
     */
    public void updateElementFlag(String id, String flag, boolean value);

    /**
     * Updates the meta information about a given element. 
     * 
     * @param id The ID of the element to update.
     * @param key The key to set.
     * @param value The value to set.
     */
    public void updateElementMetaInformation(String id, String key, String value);

    /**
     * Updates information about a HTML element.
     * 
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
}