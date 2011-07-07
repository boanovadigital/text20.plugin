/*
 * GazeEvaluatorManagerImpl.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl;

import java.util.ArrayList;
import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.PluginLoaded;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluatorManager;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFactory;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;

/**
 *
 * @author Ralf Biedert
 */
@PluginImplementation
public class GazeEvaluatorManagerImpl implements GazeEvaluatorManager {

    /** List of all plugins */
    Collection<GazeHandlerFactory> allHandler = new ArrayList<GazeHandlerFactory>();

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluatorManager#createGazeEvaluator(de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice)
     */
    @Override
    public GazeEvaluator createEvaluator(final EyeTrackingDevice trackingDevice) {

        final GazeEvaluatorImpl rval = new GazeEvaluatorImpl(this.pluginManager, trackingDevice);

        return rval;
    }

    /**
     * Called when a new gaze handler was added
     *
     * @param factory
     */
    @PluginLoaded
    public void handlerAdded(GazeHandlerFactory factory) {
        this.allHandler.add(factory);
    }

    /**
     * Returns all known handlers to mankind.
     *
     * @return .
     */
    public Collection<GazeHandlerFactory> getAllHandler() {
        return this.allHandler;
    }
}
