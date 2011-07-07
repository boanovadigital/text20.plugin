/*
 * TestSpeechRecognition.java
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

import java.net.URI;
import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.JSPFProperties;
import net.xeoh.plugins.remote.RemoteAPILipe;
import de.dfki.km.augmentedtext.services.speech.recognition.SimpleSpeechRecognitionListener;
import de.dfki.km.augmentedtext.services.speech.recognition.SpeechRecognizer;
import de.dfki.km.augmentedtext.services.speech.synthesis.SpeechSynthesis;

/**
 * @author Ralf Biedert
 *
 */
public class TestSpeechRecognition {
    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException {
        // Create plugin manager
        JSPFProperties props = new JSPFProperties();

        final PluginManager pluginManager = PluginManagerFactory.createPluginManager(props);
        pluginManager.addPluginsFrom(new URI("classpath://*"));

        RemoteAPILipe lipe = pluginManager.getPlugin(RemoteAPILipe.class);

        SpeechRecognizer plugin = lipe.getRemoteProxy(new URI("discover://youngest"), SpeechRecognizer.class);
        SpeechSynthesis synthesis = lipe.getRemoteProxy(new URI("discover://youngest"), SpeechSynthesis.class);

        synthesis.speakText("Hello World");

        plugin.addSpeechRecognitionListener(new SimpleSpeechRecognitionListener() {

            @Override
            public void newSpeechSimpleCommandId(long code) {
                System.out.println(code);
            }
        });

        pluginManager.shutdown();
    }
}
