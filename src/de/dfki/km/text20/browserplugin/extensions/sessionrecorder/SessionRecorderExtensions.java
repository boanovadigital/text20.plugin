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
package de.dfki.km.text20.browserplugin.extensions.sessionrecorder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;

/**
 * @author rb
 *
 */
@PluginImplementation
public class SessionRecorderExtensions implements Extension {

    /** */
    private SessionRecorder sessionRecorder;

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#executeFunction(java.lang.String, java.lang.String)
     */
    public Object executeDynamicFunction(String function, String args) {
        // Sanitiy check
        if (this.sessionRecorder == null) { return null; }
        try {
            if (function.equals("markLog")) {
                String s = args.substring(1, args.length() - 1);

                s = URLDecoder.decode(s, "UTF-8");

                this.sessionRecorder.markLog(s);
            }

            if (function.equals("takeScreenshot")) {
                this.sessionRecorder.takeScreenshot();
            }

            if (function.equals("mouseClicked")) {
                final String[] split = args.split(",");

                // Remove ''
                for (int i = 0; i < split.length; i++) {
                    final String string = split[i];
                    split[i] = string.replaceAll("'", "");
                }

                final int type = Integer.parseInt(URLDecoder.decode(split[0], "UTF-8"));
                final int button = Integer.parseInt(URLDecoder.decode(split[1], "UTF-8"));
                this.sessionRecorder.mouseClicked(type, button);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#getSupportedFunctions()
     */
    public String[] getDynamicFunctions() {
        return new String[] { "markLog", "takeScreenshot", "mouseClicked" };
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#setParameter(de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.SetupParameter, java.lang.Object)
     */
    public void setParameter(SetupParameter parameter, Object value) {
        if (parameter == SetupParameter.SESSION_RECORDER)
            this.sessionRecorder = (SessionRecorder) value;
    }

}
