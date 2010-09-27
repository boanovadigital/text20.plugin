/*
 * ExtensionManagerImpl.java
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
package de.dfki.km.text20.browserplugin.services.extensionmanager.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.PluginLoaded;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;

/**
 * @author rb
 */
@PluginImplementation
public class ExtensionManagerImpl implements ExtensionManager {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    private final Map<SetupParameter, Object> knownParamter = new HashMap<SetupParameter, Object>();

    /** */
    private final List<Extension> allKnownExtensions = new ArrayList<Extension>();

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.ExtensionManager#executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object executeFunction(String function, String args) {
        // TODO: Improve the loop, hash the functions instead..
        for (Extension e : this.allKnownExtensions) {
            final List<String> supported = Arrays.asList(e.getDynamicFunctions());

            if (supported.contains(function)) { return e.executeDynamicFunction(function, args); }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.ExtensionManager#setParameter(de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.SetupParameter, java.lang.Object)
     */
    @Override
    public void setParameter(SetupParameter parameter, Object value) {
        this.knownParamter.put(parameter, value);

        // Tell parameter all known children
        for (Extension e : this.allKnownExtensions) {
            e.setParameter(parameter, value);
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.ExtensionManager#getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        final List<String> rval = new ArrayList<String>();

        for (Extension e : this.allKnownExtensions) {
            final List<String> supported = Arrays.asList(e.getDynamicFunctions());
            rval.addAll(supported);
        }

        return rval;
    }

    /**
     * @param extension
     */
    @PluginLoaded
    public void newExtension(Extension extension) {
        this.logger.fine("Registered extension module " + extension.toString());

        this.allKnownExtensions.add(extension);

        // Print extension functions
        for (String string : extension.getDynamicFunctions()) {
            this.logger.fine("Module supportes extension function " + string);
        }

        // Set all parameters
        for (SetupParameter p : this.knownParamter.keySet()) {
            Object object = this.knownParamter.get(p);
            extension.setParameter(p, object);
        }
    }
}
