/*
 * TestSimpleBrainTracker.java
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

import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import net.xeoh.plugins.base.util.uri.ClassURI;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluatorManager;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeListener;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Ralf Biedert
 *
 */
public class TestSimpleEyeTracker {
    /**
     * @param args
     * @throws URISyntaxException
     * @throws InterruptedException 
     */
    public static void main(final String[] args) throws URISyntaxException,
                                                InterruptedException {

        // Load plugins, get a device, and get a evaluator
        final PluginManager pluginManager = PluginManagerFactory.createPluginManagerX().addPluginsFrom(ClassURI.CLASSPATH);
        final EyeTrackingDevice openDevice = pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities("eyetrackingdevice:trackingserver")).openDevice("discover://nearest");
        final GazeEvaluator evaluator = pluginManager.getPlugin(GazeEvaluatorManager.class).createEvaluator(openDevice);
        
        
        evaluator.addEvaluationListener(new RawGazeListener() {
            @Override
            public void newEvaluationEvent(RawGazeEvent event) {
                EyeTrackingEvent trackingEvent = event.getTrackingEvent();
                System.out.println(trackingEvent.getElapsedTime());
            }
            
            @Override
            public boolean requireUnfilteredEvents() {
                return true;
            }
        });

        System.out.println("Connected >!");

        Thread.sleep(100000);

    }
}
