/*
 * SpeechIOExtension.java
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
package de.dfki.km.text20.browserplugin.extensions.userio.speech;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Thread;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.util.InformationBrokerUtil;
import net.xeoh.plugins.remote.RemoteAPILipe;
import de.dfki.km.augmentedtext.services.speech.recognition.SimpleSpeechRecognitionListener;
import de.dfki.km.augmentedtext.services.speech.recognition.SpeechRecognizer;
import de.dfki.km.augmentedtext.services.speech.synthesis.SpeechSynthesis;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.JavaScriptExecutorItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.DynamicExtension;

/**
 * 
 * @author Ralf Biedert
 */
@PluginImplementation
public class SpeechIOExtension implements DynamicExtension {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** Recognizes speech */
    public SpeechRecognizer speechRecognizer;

    /** Synthesizes speech */
    public SpeechSynthesis speechSynthesizer;

    /** */
    protected final CountDownLatch startupLatch = new CountDownLatch(1);

    /** */
    @InjectPlugin
    public RemoteAPILipe lipe;

    /** */
    JSExecutor jsExecutor;

    /** */
    @InjectPlugin
    public InformationBroker broker;

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#
     * executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object executeDynamicFunction(String function, String args) {
        try {
            this.startupLatch.await();

            if (function.equals("speakText")) {
                String s = args.substring(1, args.length() - 1);
                s = URLDecoder.decode(s, "UTF-8");

                if (this.speechSynthesizer == null) {
                    this.logger.warning("Unable to speak text '" + s + "' as no speech synthesizer was found.");
                    return null;
                }

                this.logger.fine("Speaking '" + s + "'");
                this.speechSynthesizer.speakText(s);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#
     * getSupportedFunctions()
     */
    @Override
    public String[] getDynamicFunctions() {
        return new String[] { "speakText" };
    }

    /** Called upon init */
    @Init
    public void init() {
        this.jsExecutor = new InformationBrokerUtil(this.broker).get(JavaScriptExecutorItem.class);
    }

    /** Is in a separate thread because the lookup can take some time */
    @Thread(isDaemonic = true)
    public void setupSpecialCallback() {
        try {

            // Try to obtain synthesis and recognition
            this.logger.fine("Trying to obtain SpeechIO componentes on the network");
            try {
                this.speechRecognizer = this.lipe.getRemoteProxy(new URI("discover://youngest"), SpeechRecognizer.class);
                this.speechSynthesizer = this.lipe.getRemoteProxy(new URI("discover://youngest"), SpeechSynthesis.class);
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Debug
            if (this.speechRecognizer != null)
                this.logger.fine("Speech recognizer found");
            if (this.speechSynthesizer != null)
                this.logger.fine("Speech synthesis found");

            if (this.speechRecognizer == null && this.speechSynthesizer == null)
                this.logger.fine("No SpeechIO services found.");

            // Register hooks
            if (this.speechRecognizer != null) {
                this.logger.fine("Registering speech listener");
                this.speechRecognizer.addSpeechRecognitionListener(new SimpleSpeechRecognitionListener() {

                    /*
                     * (non-Javadoc)
                     * 
                     * @see de.dfki.km.augmentedtext.services.speech.recognition.
                     * SimpleSpeechRecognitionListener#newSpeechSimpleCommandId(long)
                     */
                    @SuppressWarnings("boxing")
                    @Override
                    public void newSpeechSimpleCommandId(final long code) {

                        SpeechIOExtension.this.logger.fine("Received speech input " + code);

                        if (SpeechIOExtension.this.jsExecutor == null) {
                            SpeechIOExtension.this.logger.warning("Unable to call back on speech input " + code);
                            return;
                        }

                        SpeechIOExtension.this.jsExecutor.executeJSFunction("specialCallback", "speech", code);
                    }
                });
                this.logger.fine("Listener added");
            } else {
                this.logger.fine("No SpeechRecognizer found. Callbacks will not work");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            this.startupLatch.countDown();
        }
    }
}
