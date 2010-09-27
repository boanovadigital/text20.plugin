/*
 * PersistantPreferences.java
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
package de.dfki.km.text20.browserplugin.services.persistentpreferences;

import net.xeoh.plugins.base.Plugin;

/**
 * Manages small and lightweight preferences of the device. It ensures values once set will stay persistent 
 * even after restart of the application.
 * 
 * @author rb
 *
 */
public interface PersistentPreferences extends Plugin {
    /**
     * Returns a previously set preference or the the default value. The default value is returned if the 
     * the key has not been set before.
     * 
     * @param key
     * @param deflt 
     * @return .
     * 
     */
    public String getString(String key, String... deflt);

    /**
     * Sets a string to the preferences. After the call returns the value will be retrievable by getString().
     * 
     * @param key
     * @param value
     */
    public void setString(String key, String value);
}
