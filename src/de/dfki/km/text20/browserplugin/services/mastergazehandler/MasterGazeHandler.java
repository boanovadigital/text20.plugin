/*
 * MasterGazeHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler;

import java.util.List;

import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.util.filter.ChainedFilter;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;

/**
 * The master gaze handler registers itself to the {@link EyeTrackingDevice} and receives raw gaze
 * data. It feeds this information to subhandlers, which in turn detect different conditions (e.g., 
 * fixations, saccades, reading, ...) and pump these events to the JavaScript layer.<br/><br/>
 * 
 * The MasterGazeHandler is only relevant in extension mode (see {@link BrowserAPI}), and even 
 * there, you only need a few of its methods (if any).
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface MasterGazeHandler {

    /**
     * Returns the list of registered JavaScript handlers for the given type of event 
     * (e.g., <code>fixation</code>, ...).
     * 
     * @param type The type to request. 
     * @return A list of JavaScript function names.
     */
    public List<String> getHandlerForType(String type);

    /**
     * Returns the main {@link GazeEvaluator} for this handler.
     * 
     * @return The used gaze evaluator.
     */
    public GazeEvaluator getGazeEvaluator();

    /**
     * Call this function to reduce the callback load for a while. The MasterGazeHandler
     * will disable high-frequency calls in order to reduce computation load in the JavaScript 
     * engine.
     * 
     * @param timeToDisable The number of miliseconds to disable JavaScript callbacks.
     */
    @Deprecated
    public void reduceJSLoad(int timeToDisable);

    /**
     * Registers a JavaScript callback handler for a given type (e.g., <code>fixation</code>, ...).
     * 
     * @param type The type to register a handler for. 
     * @param listener The name of the JavaScript listener.
     */
    public void registerJSCallback(String type, String listener);

    /**
     * Removes a handler. 
     * 
     * @param listener The name of the JavaScript handler to remove.
     */
    public void removeJSCallback(String listener);

    /**
     * Sets the tracking device for this gaze handler. When in extension mode (see {@link BrowserAPI}), this 
     * will already have been done.
     * 
     * @param trackingDevice The {@link EyeTrackingDevice} to set.
     */
    public void setTrackingDevice(EyeTrackingDevice trackingDevice);

    /**
     * Returns the master filter chain, which is a {@link ChainedFilter}. The filter is applied to raw 
     * eye tracking data before the data is being processed. 
     * 
     * @return Returns the master filter chain.
     */
    public ChainedFilter getMasterFilterChain();

}