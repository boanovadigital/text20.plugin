/*
 * Extensionmanager.java
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
package de.dfki.km.text20.browserplugin.services.extensionmanager;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;

/**
 * Finds and manages all registered {@link Extension}s. The list is constructed by the plugin providing 
 * the {@link BrowserAPI}. There should not be any need for you to use or access this class directly.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface ExtensionManager extends Plugin {

    /**
     * Executes the given function in the first extension that provides it.
     * 
     * @param function The function to execute.
     * @param args The arguments for the function.
     * 
     * @return The returned value.
     */
    public Object executeFunction(String function, String args);

    /**
     * Lists all supported extensions (the exported function names).
     * 
     * @return A list of all names.
     */
    public List<String> getExtensions();
}
