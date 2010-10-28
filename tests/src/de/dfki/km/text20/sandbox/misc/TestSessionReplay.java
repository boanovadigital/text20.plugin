package de.dfki.km.text20.sandbox.misc;

/*
 * TestSessionReplay.java
 *
 * Copyright (c) 2010, Ralf Biedert, DFKI. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

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
