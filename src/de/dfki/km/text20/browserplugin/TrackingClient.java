/*
 * TrackingClient.java
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
package de.dfki.km.text20.browserplugin;

import java.awt.AWTException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDeviceProvider;

/**
 * Starts the tracking client.
 * 
 * @author rb
 *
 */
public class TrackingClient {
    /**
     * This function is only used for testing. In reality, the BrowserPluginImpl is spawned by the browser ...
     * 
     * Nothing to see here ...
     * 
     * @param args
     * @throws AWTException
     * @throws MalformedURLException
     */
    public static void main(final String[] args) throws AWTException,
                                                MalformedURLException {

        final PluginManager pluginManager = PluginManagerFactory.createPluginManager();

        try {
            pluginManager.addPluginsFrom(new URI("classpath://de.dfki.km.augtext.tc.browserplugin.impl.BrowserPluginManagerImpl"));
            pluginManager.addPluginsFrom(new URI("classpath://de.dfki.km.augtext.tc.devices.impl.mouse.MouseTrackingDeviceProviderImpl"));
        } catch (final URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Obtain browser plugin
        // BrowserPluginManagerImpl browserPluginManager = (BrowserPluginManagerImpl) pluginManager.getPlugin(BrowserPluginManager.class);
        // browserPluginManager.setTheBrowserPlugin(null);
    }

    /**
     * @param args
     * @throws AWTException
     * @throws MalformedURLException
     * @throws URISyntaxException 
     */
    @SuppressWarnings("unused")
    public static void main_old(final String[] args) throws AWTException,
                                                    MalformedURLException,
                                                    URISyntaxException {

        final PluginManager pm = PluginManagerFactory.createPluginManager();
        pm.addPluginsFrom(new URI("classpath://*"));

        final EyeTrackingDeviceProvider provider = pm.getPlugin(EyeTrackingDeviceProvider.class);
        // TrackingDevice openDevice = provider.openDevice();
    }

}
