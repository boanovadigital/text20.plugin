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
package de.dfki.km.text20.browserplugin.extensions.misc.hacks;

import java.awt.Rectangle;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.util.InformationBrokerUtil;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.BrowserAPIItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.MasterGazeHandlerItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.PseudorendererItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.DynamicExtension;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;

/**
 * @author Ralf Biedert
 * 
 */
@PluginImplementation
public class VariousHacksExtension implements DynamicExtension {

    /** */
    private MasterGazeHandler gazeHandler;

    /** */
    private BrowserAPI browserAPI;

    /** */
    private Pseudorenderer pseudorender;

    /** */
    @InjectPlugin
    public InformationBroker broker;

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#
     * executeFunction(java.lang.String, java.lang.String)
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

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#
     * getSupportedFunctions()
     */
    @Override
    public String[] getDynamicFunctions() {
        return new String[] { "reduceJSLoad", "invalidateRectangle" };
    }

    /** Called upon init */
    @Init
    public void init() {
        this.gazeHandler = new InformationBrokerUtil(this.broker).get(MasterGazeHandlerItem.class);
        this.browserAPI = new InformationBrokerUtil(this.broker).get(BrowserAPIItem.class);
        this.pseudorender = new InformationBrokerUtil(this.broker).get(PseudorendererItem.class);
    }
}
