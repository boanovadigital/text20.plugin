/*
 * PersistentPreferencesImpl.java
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
package de.dfki.km.text20.browserplugin.services.persistentpreferences.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import de.dfki.km.text20.browserplugin.services.persistentpreferences.PersistentPreferences;

/**
 * @author rb
 */
@PluginImplementation
public class PersistentPreferencesImpl implements PersistentPreferences {

    // This fuck rejects long keys!
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());

    /**
     * @param str
     * @return
     */
    @SuppressWarnings("boxing")
    private static String hash(final String str) {
        // Try to generate hash
        try {

            final MessageDigest digest = java.security.MessageDigest.getInstance("MD5");

            final byte[] data = str.getBytes();

            digest.update(data, 0, data.length);
            final byte[] hash = digest.digest();

            // Assemble hash string
            final StringBuilder sb = new StringBuilder();
            for (final byte b : hash) {
                final String r = String.format("%02x", b);
                sb.append(r);
            }

            return sb.toString();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.persistentpreferences.PersistentPreferences#getString(java.lang.String, java.lang.String[])
     */
    @Override
    public String getString(final String key, final String... deflt) {
        if (deflt.length > 0) return this.prefs.get(hash(key), deflt[0]);
        return this.prefs.get(hash(key), "value unknown");
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.persistentpreferences.PersistentPreferences#setString(java.lang.String, java.lang.String)
     */
    @Override
    public void setString(final String key, final String value) {

        try {
            this.prefs.put(hash(key), value);
            this.prefs.flush();
        } catch (final BackingStoreException e) {
            e.printStackTrace();
        } catch (final IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
