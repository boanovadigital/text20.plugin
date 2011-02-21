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

import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.util.filter.ChainedFilter;

/**
 * A master gaze handler registers itself to the tracking device and receives raw gaze
 * data. It feeds this information into other plugins (subhandlers) which in turn detect
 * different conditions (fixations, ...). A given JSHandler is used to push our events to
 * JavaScript.
 * 
 * @author Ralf Biedert
 */
public interface MasterGazeHandler {

    /**
     * Returns a handler for the given type of event.
     * 
     * @param type
     * @return .
     */
    public List<String> getHandlerForType(String type);

    /**
     * Returns the gaze evaluator for this handler.
     * 
     * @return The used gaze evaluator.
     */
    public GazeEvaluator getGazeEvaluator();

    /**
     * Call this function to reduce the callback load for a while. The master gaze handler
     * will disable high-frequency calls in order to reduce computation load in the JS
     * engine.
     * 
     * @param timeToDisable The number of miliseconds to disable the JavaScript callbacks.
     */
    public void reduceJSLoad(int timeToDisable);

    /**
     * Registers a callback handler
     * 
     * @param type
     * @param listener
     */
    public void registerJSCallback(String type, String listener);

    /**
     * Remove a handler
     * 
     * @param listener
     */
    public void removeJSCallback(String listener);

    /**
     * Sets the tracking device for this gaze handler.
     * 
     * @param trackingDevice
     */
    public void setTrackingDevice(EyeTrackingDevice trackingDevice);

    /**
     * Returns the master filter chain .
     * 
     * @return .
     */
    public ChainedFilter getMasterFilterChain();

}