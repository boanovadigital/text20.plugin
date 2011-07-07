/*
 * BackgroundServicesExtension.java
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
package de.dfki.km.text20.browserplugin.extensions.misc.backgroundservices;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Thread;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.util.InformationBrokerUtil;
import net.xeoh.plugins.remote.RemoteAPILipe;
import de.dfki.km.augmentedtext.services.datasource.dbpedia.DBPedia;
import de.dfki.km.augmentedtext.services.language.statistics.Statistics;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.JavaScriptExecutorItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.DynamicExtension;

/**
 * 
 * @author Ralf Biedert
 */
@PluginImplementation
public class BackgroundServicesExtension implements DynamicExtension {

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** DBPedia */
    public DBPedia dbpedia;

    /** Statistics */
    public Statistics statistics;

    /** */
    protected final CountDownLatch startupLatch = new CountDownLatch(1);

    /** */
    @InjectPlugin
    public RemoteAPILipe lipe;

    /** */
    @InjectPlugin
    public InformationBroker broker;

    /** */
    JSExecutor jsExecutor;

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.extensionmanager.Extension#
     * executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    @SuppressWarnings("boxing")
    public Object executeDynamicFunction(String function, String args) {
        try {
            this.startupLatch.await();

            if (function.equals("getAbstract")) {
                String s = args.substring(1, args.length() - 1);
                s = URLDecoder.decode(s, "UTF-8");

                if (this.dbpedia == null) {
                    this.logger.warning("Unable to get abstract for '" + s + "' as no DBPedia was found.");
                    return null;
                }

                this.logger.fine("Getting abstract for '" + s + "'");
                return this.dbpedia.getAbstract(s);
            }

            if (function.equals("getProbability")) {
                String s = args.substring(1, args.length() - 1);
                s = URLDecoder.decode(s, "UTF-8");

                if (this.statistics == null) {
                    this.logger.warning("Unable to get probability for '" + s + "' as no DBPedia was found.");
                    return null;
                }

                this.logger.fine("Getting abstract for '" + s + "'");
                return this.statistics.getProbabilities(s)[0];
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
        return new String[] { "getAbstract", "getProbability" };
    }

    /** Called upon init */
    @Init
    public void init() {
        this.jsExecutor = new InformationBrokerUtil(this.broker).get(JavaScriptExecutorItem.class);
    }

    /** Is in a separate thread because the lookup can take some time */
    @Thread(isDaemonic = true)
    public void setupSpecialCallback() {
        try {
            // Try to obtain synthesis and recognition
            this.logger.fine("Trying to obtain BackgroundServices componentes on the network");
            try {
                this.dbpedia = this.lipe.getRemoteProxy(new URI("discover://nearest"), DBPedia.class);
                this.statistics = this.lipe.getRemoteProxy(new URI("discover://nearest"), Statistics.class);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            // Debug
            if (this.dbpedia != null) this.logger.fine("DBPedia found");
            if (this.statistics != null) this.logger.fine("Statistics found");

            if (this.dbpedia == null && this.statistics == null)
                this.logger.fine("No background services services found.");
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            this.startupLatch.countDown();
        }
    }

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(URLDecoder.decode("Rainer%20Br%FCderle", "ISO-8859-1"));
        System.out.println(URLDecoder.decode("Rainer%20Br%FCderle", "UTF-8"));
        System.out.println(URLDecoder.decode("Rainer%20Br%C3%BCderle", "UTF-8"));
    }
}
