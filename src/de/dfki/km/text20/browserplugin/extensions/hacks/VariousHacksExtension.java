/*
 * VariousHacksExtension.java
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
package de.dfki.km.text20.browserplugin.extensions.hacks;

import java.awt.Rectangle;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;

/**
 * @author rb
 *
 */
@PluginImplementation
public class VariousHacksExtension implements Extension {

    /** */
    private MasterGazeHandler gazeHandler;

    /** */
    private BrowserAPI browserAPI;

    /** */
    private Pseudorenderer pseudorender;

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object executeDynamicFunction(String function, String args) {
        try {
            if (function.equals("reduceJSLoad")) {
                String s = args.substring(1, args.length() - 1);
                s = URLDecoder.decode(s, "UTF-8");
                this.gazeHandler.reduceJSLoad(Integer.parseInt(s));
            }

            if (function.equals("invalidateRectangle")) {
                // String s = args.substring(1, args.length() - 1);

                // Remove ''
                final String[] split = args.split(",");
                for (int i = 0; i < split.length; i++) {
                    final String string = split[i];
                    split[i] = string.replaceAll("'", "");
                }

                final int x = Integer.parseInt(URLDecoder.decode(split[0], "UTF-8"));
                final int y = Integer.parseInt(URLDecoder.decode(split[1], "UTF-8"));
                final int w = Integer.parseInt(URLDecoder.decode(split[2], "UTF-8"));
                final int h = Integer.parseInt(URLDecoder.decode(split[3], "UTF-8"));

                final Collection<RenderElement> allElementsIntersecting = this.pseudorender.getAllElementsIntersecting(new Rectangle(x, y, w, h), CoordinatesType.DOCUMENT_BASED);

                for (final RenderElement renderElement : allElementsIntersecting) {
                    this.browserAPI.updateElementFlag(renderElement.getIdentifier(), "INVALID", true);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#getSupportedFunctions()
     */
    @Override
    public String[] getDynamicFunctions() {
        return new String[] { "reduceJSLoad", "invalidateRectangle" };
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#setParameter(de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.SetupParameter, java.lang.Object)
     */
    @Override
    public void setParameter(SetupParameter parameter, Object value) {
        if (parameter == SetupParameter.GAZE_HANDLER)
            this.gazeHandler = (MasterGazeHandler) value;

        if (parameter == SetupParameter.BROWSER_API)
            this.browserAPI = (BrowserAPI) value;

        if (parameter == SetupParameter.PSEUDO_RENDERER)
            this.pseudorender = (Pseudorenderer) value;

    }

}
