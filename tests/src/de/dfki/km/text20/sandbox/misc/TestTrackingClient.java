/*
 * TestTrackingClient.java
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
import net.xeoh.plugins.base.options.getplugin.OptionCapabilities;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;
import de.dfki.km.text20.trackingserver.eyes.remote.TrackingCommand;

/**
 * @author rb
 *
 */
public class TestTrackingClient {
    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(final String[] args) throws URISyntaxException {

        // Load plugins
        final PluginManager pluginManager = PluginManagerFactory.createPluginManager();
        pluginManager.addPluginsFrom(new URI("classpath://*"));

        // Obtain the proper tracking device
        final EyeTrackingDeviceProvider deviceProvider = pluginManager.getPlugin(EyeTrackingDeviceProvider.class, new OptionCapabilities("eyetrackingdevice:trackingserver"));
        final EyeTrackingDevice openDevice = deviceProvider.openDevice("discover://nearest");

        System.out.println(">> " + openDevice);

        if (openDevice == null) return;

        // Print data
        openDevice.addTrackingListener(new EyeTrackingListener() {

            @Override
            public void newTrackingEvent(final EyeTrackingEvent event) {
                float[] headPosition = event.getHeadPosition();
                System.out.println();
                for (float f : headPosition) {
                    System.out.println(" * " + f);
                }
            }
        });

        openDevice.sendLowLevelCommand(TrackingCommand.HARDWARE_CALIBRATION);
    }
}
