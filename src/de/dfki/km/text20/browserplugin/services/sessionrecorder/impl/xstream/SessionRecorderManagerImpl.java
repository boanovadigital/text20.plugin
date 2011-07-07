/*
 * SessionRecorderManagerImpl.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.Capabilities;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Shutdown;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;

/**
 * @author Ralf Biedert
 *
 */
@PluginImplementation
public class SessionRecorderManagerImpl implements SessionRecorderManager {

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /** List with all event recorder */
    final List<SessionRecorderImpl> allRecorder = new ArrayList<SessionRecorderImpl>();

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorderManager#createSessionRecorder()
     */
    @Override
    public SessionRecorder createSessionRecorder(CreateRecorderOption... options) {
        final SessionRecorderImpl streamer = new SessionRecorderImpl(this.pluginManager, options);

        synchronized (this.allRecorder) {
            this.allRecorder.add(streamer);
        }

        return streamer;
    }

    /** */
    @Shutdown
    public void shutdown() {
        synchronized (this.allRecorder) {
            for (final SessionRecorderImpl streamer : this.allRecorder) {
                streamer.shutdown();
            }
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionRecorderManager#loadSessionReplay(java.io.File)
     */
    @Override
    public SessionReplay loadSessionReplay(File file) {
        return new SessionReplayImpl(file);
    }

    /**
     * Return what we can do...
     *
     * @return .
     */
    @Capabilities
    public String[] getCapabilities() {
        return new String[] { "sessionrecorder:xstream" };
    }

}
