/*
 * SessionRecorder.java
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
 * Manages the recording of interactive gaze-based session. The recorder can also be 
 * used to fake sessions, in that case, <code>specialCommand()</code> should be called prior
 * to <b>each</b> other method call to fake the next date. 
 * 
 * @author Ralf Biedert
 * @sice 1.0
 */
public interface SessionRecorder {
    /**
     * Records the call to a function.
     * 
     * @param function The function name to record.
     */
    public void callFunction(String function);

    /**
     * Records a call back to JavaScript.
     * 
     * @param function The called function.
     * @param args The arguments.
     */
    public void executeJSFunction(String function, Object... args);

    /**
     * Records a preference that was requested.
     * 
     * @param key They key.
     * @param deflt The default.
     */
    public void getPreference(String key, String deflt);

    /**
     * Puts a mark into the log. 
     * 
     * @param subSequence Name of the mark. 
     */
    public void markLog(String subSequence);

    /**
     * Sets a mouse click.
     * 
     * @param type Singleclick = 1, doubleclick = 2, ...
     * @param button The button that was pressed.
     */
    public void mouseClicked(int type, int button);

    /**
     * Puts a new eye tracking event.
     * 
     * @param event The event to put.
     */
    public void eyeTrackingEvent(EyeTrackingEvent event);

    /**
     * Puts a new brain tracking event.
     * 
     * @param event The event to put.
     */
    public void brainTrackingEvent(BrainTrackingEvent event);

    /**
     * A new listener was added.
     * 
     * @param type Type of the listener. 
     * @param listener Name. 
     */
    public void registerListener(String type, String listener);

    /**
     * A listener was removed.
     * 
     * @param listener Name.
     */
    public void removeListener(String listener);

    /**
     * Stored a parameter.
     * 
     * @param key The key. 
     * @param value The value.
     * 
     */
    public void setParameter(String key, String value);

    /**
     * Preference was set.
     * 
     * @param key The key.
     * @param value The value.
     */
    public void setPreference(String key, String value);

    /**
     * Indicates the recording should start. Must be called first. 
     */
    public void start();

    /**
     * Indicates the recording should stop. Must be called last. Recordings are somewhat 
     * fail-safe, thus an application does not need to call stop() at any cost. However, in that
     * case some events might be missing (usually 3-5 seconds). 
     */
    public void stop();

    /**
     * Stores the eye device info.
     * 
     * @param deviceInfo Device info to store. 
     */
    public void storeEyeDeviceInfo(EyeTrackingDeviceInfo deviceInfo);

    /**
     * Stores the brain device info.
     * 
     * @param deviceInfo Device info to store.
     */
    public void storeBrainDeviceInfo(BrainTrackingDeviceInfo deviceInfo);

    /**
     * Tells the recorder to take a screenshot.
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
     * 
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
     * Set the current mouse position in screen coordinates.
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
