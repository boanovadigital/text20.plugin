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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.pseudo.PseudoImageEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionLoadImages;

/**
 * @author buhl
 *
 */
public class TestSessionReplay {

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    private PluginManager pluginManager;

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(final String[] args) throws URISyntaxException {
        TestSessionReplay rp = new TestSessionReplay();
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

        final File file = new File("../Text 2.0 Experiment Results/quickskim.prestudy.2010/experiment.3.zip");

        System.out.println();
        System.out.println("Events come next");

        // Load the player and replay
        final SessionReplay player = this.pluginManager.getPlugin(SessionRecorderManager.class, new OptionCapabilities("sessionrecorder:xstream")).loadSessionReplay(file);
        player.replay(new ReplayListener() {

            @SuppressWarnings("unused")
            @Override
            public void nextEvent(AbstractSessionEvent event) {
                if (event instanceof PseudoImageEvent) {
                    PseudoImageEvent e = (PseudoImageEvent) event;
                }
            }
        }, new OptionLoadImages());

        System.out.println();
        System.out.println("Props come next");

        // Print infos
        final Map<String, String> props = player.getProperties();
        for (String key : props.keySet()) {
            System.out.println(key + ": " + props.get(key));
        }

        System.out.println(player.getScreenSize());

        this.pluginManager.shutdown();
    }
}
