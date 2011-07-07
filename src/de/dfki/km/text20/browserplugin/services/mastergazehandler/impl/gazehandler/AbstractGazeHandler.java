/*
 * AbstractGazeHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler;

import net.xeoh.plugins.base.PluginManager;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Base class of all gaze handler
 * 
 * @author Ralf Biedert
 * 
 */
public abstract class AbstractGazeHandler {
    /** Main plugin manager */
    protected PluginManager pluginManager;
    
    /** The executor to trigger JavaScript */
    protected JSExecutor browserPlugin;

    /** Keeps references to handlers */
    protected MasterGazeHandler masterGazeHandler;

    /** Pseudo rendering surface equal to the window receiving gaze events */
    protected Pseudorenderer pseudorenderer;

    
    /** If set, be as quiet as possible */
    protected boolean reducedCommunication = false;


    /**
     * Init the gaze handler
     * 
     * @param mgr 
     * @param master
     * @param pr
     * @param bp
     * @param evaluator
     */
    public void init(final PluginManager mgr, final MasterGazeHandler master, final Pseudorenderer pr,
                     final JSExecutor bp, final GazeEvaluator evaluator) {
        this.pluginManager = mgr;
        this.masterGazeHandler = master;
        this.pseudorenderer = pr;
        this.browserPlugin = bp;

        // Fallback if now browser plugin is supplied
        if (this.browserPlugin == null) {
            this.browserPlugin = new JSExecutor() {

                @Override
                public Object executeJSFunction(String function, Object... args) {
                    return null;
                }
            };
        }

        registerToEvaluator(evaluator);
    }

    /**
     * Sets this plugin to reduced communication. In that case, plugins should only
     * transmit what is absolutely neccessary.
     * 
     * @param b
     */
    public void setReducedCommunication(final boolean b) {
        this.reducedCommunication = b;
    }

    /**
     * We require the handler to register themselves to some evaluator.
     * 
     * @param evaluator
     */
    protected abstract void registerToEvaluator(GazeEvaluator evaluator);
}
