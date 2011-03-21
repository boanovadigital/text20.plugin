/*
 * OS.java
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
package de.dfki.km.text20.util.system;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Code shamelessly stolen from the net.
 * 
 * @author ?
 *
 */
public class OS {

    /**
     * @param path
     * @return .
     */
    public static URI absoluteBrowserPathToURI(String path) {
        try {
            return new URI(path.replaceAll(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @return .
     */
    public static String getOsName() {
        return System.getProperty("os.name", "unknown");
    }

    /**
     * @return .
     */
    public static boolean isAix() {
        return getOsName().toLowerCase().indexOf("aix") >= 0;
    }

    /**
     * @return .
     */
    public static boolean isLinux() {
        return getOsName().toLowerCase().indexOf("linux") >= 0;
    }

    /**
     * @return .
     */
    public static boolean isMac() {
        final String os = getOsName().toLowerCase();
        return os.startsWith("mac") || os.startsWith("darwin");
    }

    /**
     * @return .
     */
    public static boolean isSolaris() {
        final String os = getOsName().toLowerCase();
        return os.indexOf("sunos") >= 0;
    }

    /**
     * @return .
     */
    public static boolean isUnix() {
        final String os = getOsName().toLowerCase();

        // XXX: this obviously needs some more work to be "true" in general (see bottom of file)
        if (os.indexOf("sunos") >= 0 || os.indexOf("linux") >= 0) return true;

        if (isMac() && System.getProperty("os.version", "").startsWith("10."))
            return true;

        return false;
    }

    /**
     * @return .
     */
    public static boolean isWindows() {
        return getOsName().toLowerCase().indexOf("windows") >= 0;
    }
}
