/*
 * SessionReplay.java
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

import java.awt.Dimension;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * The SessionReplay is responsible for replaying previously recorded session (mostly XML streams) when 
 * the framework was used as a web application (see {@link BrowserAPI}).    
 * 
 * @author Ralf Biedert
 * @since 1.3
 */
public interface SessionReplay {
    /**
     * Replays the session with the given listener.
     * 
     * @param listener The lister that will receive the {@link AbstractSessionEvent}s.
     * @param options Options for this replay. 
     */
    public void replay(ReplayListener listener, ReplayOption... options);

    /**
     * Returns the screen size of this replay.
     *  
     * @return The screen size of the original screen.
     */
    public Dimension getScreenSize();

    /**
     * Returns the properties saved in the session. 
     * 
     * @param properties If specified, only these properties are being returned (if any). In that case the 
     * method is likely to be faster.
     * @return A Map containing all requested properties.
     */
    public Map<String, String> getProperties(String... properties);

    /**
     * Returns a list of (usually) manually created displacements for this file, which are
     * used to correct off gaze data.  
     * 
     * @return The list of displacements.
     */
    @Deprecated
    public List<DisplacementRegion> getDisplacements();
    
    /**
     * Returns a resource for this replay (e.g., an image) that was also stored along with the replay. 
     *
     * @param resource The requested resource name (e.g., <code>"image.spacer.gif"</code>). 
     * @return A stream for the resource.
     */
    public InputStream getResource(String resource);
}
