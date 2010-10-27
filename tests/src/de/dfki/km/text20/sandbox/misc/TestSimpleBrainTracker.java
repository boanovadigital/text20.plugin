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

import java.net.URI;
import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;

/**
 * @author rb
 *
 */
public class TestSimpleBrainTracker {
    /**
     * @param args
     * @throws URISyntaxException
     * @throws InterruptedException 
     */
    public static void main(final String[] args) throws URISyntaxException,
                                                InterruptedException {

        // Load plugins
        final PluginManager pluginManager = PluginManagerFactory.createPluginManager();
        pluginManager.addPluginsFrom(new URI("classpath://*"));

        final BrainTrackingDevice openDevice = pluginManager.getPlugin(BrainTrackingDeviceProvider.class).openDevice("discover://nearest");
        openDevice.addTrackingListener(new BrainTrackingListener() {
            @Override
            public void newTrackingEvent(BrainTrackingEvent event) {
                System.out.println(event);
            }
        });

        System.out.println("Connected!");

        Thread.sleep(100000);

    }
}
