/*
 * SessionRecorderManager.java
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

import java.io.File;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;

/**
 * The SessionRecorderManager enables you to record and replay interaction session. In most cases you 
 * want to replay a previously recorded session. 
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface SessionRecorderManager extends Plugin {

    /**
     * Creates a {@link SessionRecorder} which can be used to write the current session log into a file. 
     *  
     * @param options The options for the recorder to create. 
     * @return A new recorder object.
     */
    SessionRecorder createSessionRecorder(CreateRecorderOption... options);

    /**
     * Loads a {@link SessionReplay} from a given path.
     * 
     * @param file The file/path to load the replay from. 
     * @return A new replay object.
     */
    SessionReplay loadSessionReplay(File file);
}