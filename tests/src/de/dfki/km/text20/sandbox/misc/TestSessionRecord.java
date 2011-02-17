/*
 * TestSessionReplay.java
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
package de.dfki.km.text20.sandbox.misc;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.net.URI;
import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.createrecorder.OptionFakeReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.specialcommand.OptionFakeNextDate;

/**
 * @author Ralf Biedert
 *
 */
public class TestSessionRecord {

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    private PluginManager pluginManager;

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(final String[] args) throws URISyntaxException {
        TestSessionRecord rp = new TestSessionRecord();
        rp.init();
        System.exit(0);
    }

    /**
     * @throws URISyntaxException
     *
     */
    public void init() throws URISyntaxException {

        this.pluginManager = PluginManagerFactory.createPluginManager();
        this.pluginManager.addPluginsFrom(new URI("classpath://*"));


        // Load the player and replay
        final SessionRecorderManager manager = this.pluginManager.getPlugin(SessionRecorderManager.class, new OptionCapabilities("sessionrecorder:xstream"));
        final SessionRecorder recorder = manager.createSessionRecorder(new OptionFakeReplay("/tmp/1.zip", new Dimension(800, 600), 100));
        
        recorder.start();
        
        recorder.specialCommand(new OptionFakeNextDate(101));
        recorder.updateGeometry(new Rectangle(1, 2, 3, 4));
        
        recorder.stop();

        this.pluginManager.shutdown();
    }
}
