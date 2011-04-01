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

import static net.jcores.CoreKeeper.$;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.uri.ClassURI;
import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;

/**
 * @author buhl
 *
 */
public class TestExtensions {

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    private PluginManager pluginManager;

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * @param args
     * @throws URISyntaxException
     */
    public static void main(final String[] args) throws URISyntaxException {
        TestExtensions rp = new TestExtensions();
        rp.init();
        System.exit(0);
    }

    /**
     * @throws URISyntaxException
     *
     */
    public void init() throws URISyntaxException {

        this.pluginManager = PluginManagerFactory.createPluginManager();
        this.pluginManager.addPluginsFrom(ClassURI.CLASSPATH);

        ExtensionManager manager = this.pluginManager.getPlugin(ExtensionManager.class);
        Object executeFunction = manager.executeFunction("test", "(12)");
        $(manager.getExtensions()).print();
        
        System.out.println(executeFunction);

        this.pluginManager.shutdown();
    }
}
