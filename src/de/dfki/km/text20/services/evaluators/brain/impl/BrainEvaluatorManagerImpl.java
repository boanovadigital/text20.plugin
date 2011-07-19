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
package de.dfki.km.text20.services.evaluators.brain.impl;

import static net.jcores.jre.CoreKeeper.$;

import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.PluginLoaded;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluator;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluatorManager;
import de.dfki.km.text20.services.evaluators.brain.BrainHandlerFactory;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;

/**
 *
 * @author Ralf Biedert
 */
@PluginImplementation
public class BrainEvaluatorManagerImpl implements BrainEvaluatorManager {
    /** List of all plugins */
    Collection<BrainHandlerFactory> allHandler = $.list();

    /** */
    @InjectPlugin
    public PluginManager pluginManager;

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluatorManager#createGazeEvaluator(de.dfki.km.augmentedtext.services.trackingdevices.TrackingDevice)
     */
    @Override
    public BrainEvaluator createEvaluator(final BrainTrackingDevice trackingDevice) {
        return new BrainEvaluatorImpl(this.pluginManager, trackingDevice);
    }

    /**
     * Called when a new gaze handler was added
     *
     * @param factory
     */
    @PluginLoaded
    public void handlerAdded(BrainHandlerFactory factory) {
        this.allHandler.add(factory);
    }

    /**
     * Returns all known handlers to mankind.
     *
     * @return .
     */
    public Collection<BrainHandlerFactory> getAllHandler() {
        return this.allHandler;
    }
}
