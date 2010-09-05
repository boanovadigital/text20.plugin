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
package de.dfki.km.text20.services.evaluators.gaze.util.handler;

import java.util.ArrayList;
import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationEvent;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFlags;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;

/**
 * @author Ralf Biedert
 *
 * @param <E>
 * @param <L>
 */
public abstract class AbstractGazeHandler<E extends GazeEvaluationEvent, L extends GazeEvaluationListener<E>>
        implements EyeTrackingListener, GazeHandler {

    /** Related listener */
    protected final L attachedListener;

    /** The related gaze evaluator */
    protected GazeEvaluator gazeEvaluator;

    /** Plugin manager */
    protected PluginManager pluginManager;

    /**
     * @param listener
     */
    public AbstractGazeHandler(final L listener) {
        this.attachedListener = listener;
    }

    /**
     * Calls the attached listener with an event.
     *
     * @param event
     */
    public void callListener(final E event) {
        this.attachedListener.newEvaluationEvent(event);
    }

    /**
     * Called after all fields have been set and gives the handler a chance to use these
     * fields to set itself up.
     *
     * @param options
     */
    public void init(AddGazeEvaluationListenerOption... options) {
        //
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener#newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public void newTrackingEvent(final EyeTrackingEvent filteredEvent) {
        // Nothing to see here, move on.
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.gazeevaluator.GazeHandler#getFlags()
     */
    @Override
    public Collection<GazeHandlerFlags> getFlags() {
        return new ArrayList<GazeHandlerFlags>();
    }

    /**
     * @param gazeEvaluator the gazeEvaluator to set
     */
    public void setGazeEvaluator(final GazeEvaluator gazeEvaluator) {
        this.gazeEvaluator = gazeEvaluator;
    }

    /**
     * @param pluginManager the pluginManager to set
     */
    public void setPluginManager(final PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }
}
