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
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Creates a new master gaze handler.
 *
 * @author rb
 */
public interface MasterGazeHandlerManager extends Plugin {
    /**
     *
     * @param browserPlugin
     * @param pseudorenderer
     * @return .
     */
    MasterGazeHandler createMasterGazeHandler(JSExecutor browserPlugin,
                                              Pseudorenderer pseudorenderer);
}