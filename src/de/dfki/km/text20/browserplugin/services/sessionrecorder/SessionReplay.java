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
import java.util.List;
import java.util.Map;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * @author Ralf Biedert
 *
 */
public interface SessionReplay {
    /**
     * @param listener
     * @param options 
     */
    public void replay(ReplayListener listener, ReplayOption... options);

    /**
     * Returns the screen size of this replay.
     *  
     * @return .
     */
    public Dimension getScreenSize();

    /**
     * Returns the properties saved in the session. 
     * 
     * @param properties If specified, only these are returned (if any). In that case the method is likely to be faster.
     * @return .
     */
    public Map<String, String> getProperties(String... properties);

    /**
     * Returns a list of (usually) manually created displacements for this file, which are
     * used to correct off gaze data.  
     * 
     * @return .
     */
    public List<DisplacementRegion> getDisplacements();
}
