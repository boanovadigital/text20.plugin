/*
 * MasterGazeHandlerManager.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Manages {@link MasterGazeHandler}s. In most cases there is only one 
 * handler, and when in extension mode it is already there (see {@link BrowserAPI}),
 * and when in library mode, you won't need it.  
 *
 * @author Ralf Biedert
 * @since 1.0
 */
public interface MasterGazeHandlerManager extends Plugin {
    /**
     * Creates a new {@link MasterGazeHandler} for the given {@link JSExecutor} and {@link Pseudorenderer}.
     *
     * @param executor The {@link JSExecutor} to call the JavaScript engine.
     * @param pseudorenderer The current {@link Pseudorenderer}.
     * @return A newly created {@link MasterGazeHandler}.
     */
    MasterGazeHandler createMasterGazeHandler(JSExecutor executor,
                                              Pseudorenderer pseudorenderer);
}