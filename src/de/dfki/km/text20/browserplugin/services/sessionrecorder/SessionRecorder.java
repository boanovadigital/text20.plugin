/*
 * Gazerecorder.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder;

import java.awt.Point;
import java.awt.Rectangle;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Manages the recording of interactive gaze-based session. 
 * 
 * 
 * @author rb
 *
 */
public interface SessionRecorder {
    /**
     * Records the call to a function.
     * 
     * @param function
     */
    public void callFunction(String function);

    /**
     * 
     * @param function
     * @param args
     */
    public void executeJSFunction(String function, Object... args);

    /**
     * Preference was requested.
     * 
     * @param key
     * @param deflt
     */
    public void getPreference(String key, String deflt);

    /**
     * Puts a mark into the log.
     * 
     * @param subSequence
     */
    public void markLog(String subSequence);

    /**
     * Sets a mouse click
     * 
     * @param type Singleclick = 1, doubleclick = 2, ... 
     * @param button 
     * 
     * 
     */
    public void mouseClicked(int type, int button);

    /**
     * Puts a new tracking event.
     * 
     * @param event
     */
    public void newTrackingEvent(EyeTrackingEvent event);

    /**
     * Puts a new tracking event.
     * 
     * @param event
     */
    public void newBrainTrackingEvent(BrainTrackingEvent event);

    /**
     * A new listener was added
     * @param type
     * @param listener
     */
    public void registerListener(String type, String listener);

    /**
     * Listener was removed
     * 
     * @param listener
     */
    public void removeListener(String listener);

    /**
     * Stores a parameter
     * 
     * @param key 
     * @param value 
     * 
     */
    public void setParameter(String key, String value);

    /**
     * Preference was set.
     * 
     * @param key
     * @param value
     */
    public void setPreference(String key, String value);

    /**
     * Starts recording a session
     */
    public void start();

    /**
     * Stops recording of a session
     */
    public void stop();

    /**
     * Stores the device info.
     * 
     * @param deviceInfo
     */
    public void storeDeviceInfo(EyeTrackingDeviceInfo deviceInfo);

    /**
     * Stores the device info.
     * 
     * @param deviceInfo
     */
    public void storeBrainDeviceInfo(BrainTrackingDeviceInfo deviceInfo);

    /**
     * Tells the recorder to take a screenshot.
     * 
     */
    public void takeScreenshot();

    /**
     * Element flag was updated.
     * 
     * @param id
     * @param flag
     * @param value
     */
    public void updateElementFlag(String id, String flag, boolean value);

    /**
     * Updates the windowGeometry of one element
     * @param id
     * @param type
     * @param content
     * @param r
     * 
     */
    public void updateElementGeometry(String id, String type, String content, Rectangle r);

    /**
     * Sets new windowGeometry of the tracked main window.
     * 
     * @param rectangle
     */
    public void updateGeometry(Rectangle rectangle);

    /**
     * Set the current mouse position
     * 
     * @param x 
     * @param y 
     * 
     */
    public void updateMousePosition(int x, int y);

    /**
     * Sets new viewport of the application
     * 
     * @param viewportStart
     */
    public void updateViewport(Point viewportStart);

    /**
     * @param id
     * @param key
     * @param value
     */
    public void updateElementMetaInformation(String id, String key, String value);

    /**
     * Executes a special command (usually not needed)
     * 
     * @param options
     */
    public void specialCommand(SpecialCommandOption... options);
}
