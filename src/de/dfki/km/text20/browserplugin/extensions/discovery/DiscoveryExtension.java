/*
 * ScreenShotExtension.java
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
package de.dfki.km.text20.browserplugin.extensions.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.logging.Logger;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.remote.PublishMethod;
import net.xeoh.plugins.remotediscovery.DiscoveredPlugin;
import net.xeoh.plugins.remotediscovery.RemoteDiscovery;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;

/**
 * @author rb
 *
 */
@PluginImplementation
public class DiscoveryExtension implements Extension {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    @InjectPlugin
    public RemoteDiscovery remoteDiscovery;

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#executeFunction(java.lang.String, java.lang.String)
     */
    public Object executeDynamicFunction(String function, String args) {

        
        // Invokes the discovery service to find a service on the network.
        if (function.equals("obtainServiceURL")) {

            this.logger.fine("Got request to obtainServiceURL()");
            
            final String[] split = args.split(",");
            
            // Remove ''
            for (int i = 0; i < split.length; i++) {
                final String string = split[i];

                split[i] = string.trim();
                split[i] = split[i].replaceAll("'", "");
            }

            // Get protocol and UI from args
            final String protocol = split[0];
            final String uri = split[1];

            this.logger.fine("Protocol " + protocol);
            this.logger.fine("URI " + uri);
            
            // Get path
            String path = "/UNDEFINED";

            try {
                final URI requestURI = new URI(uri);
                path = requestURI.getPath();
                final int i = path.lastIndexOf('/');
                if (i >= 0) {
                    path = path.substring(i);
                }
            } catch (final URISyntaxException e) {
                e.printStackTrace();
            }
            
            this.logger.fine("Path " + path);

            

            // FIXME: This is currently a bit broken. If we do not specify args (aka discover://any) the 
            // Discovery is allowed to return just any suitable plugin (and might skip some). We have to
            // specifiy OptionDiscoverAllLocal first, do a quick search, and if we don't find anything
            // return the check with OptionDiscoverAll.
            
            
            // Get all plugins and decide manually.
            final Collection<DiscoveredPlugin> discover = this.remoteDiscovery.discover(Plugin.class);
            
            this.logger.fine("Found " + discover.size() + " plugins");

            for (final DiscoveredPlugin discoveredPlugin : discover) {
                final PublishMethod method = discoveredPlugin.getPublishMethod();
                final URI publishURI = discoveredPlugin.getPublishURI();
                
                this.logger.fine("-> Found " + publishURI);

                final boolean a = method.toString().equals(protocol);
                final boolean b = path.equals(publishURI.getPath());

                if (a && b) {
                    this.logger.fine("Returning " + publishURI);                    
                    return publishURI.toString();
                }
            }

            return null;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#getSupportedFunctions()
     */
    public String[] getDynamicFunctions() {
        return new String[] { "obtainServiceURL" };
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#setParameter(de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.SetupParameter, java.lang.Object)
     */
    public void setParameter(SetupParameter parameter, Object value) {
        //
    }

}
