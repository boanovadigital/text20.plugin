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
package de.dfki.km.text20.services.evaluators.brain.util.handler;

import static net.jcores.jre.CoreKeeper.$;

import java.util.Collection;

import net.xeoh.plugins.base.PluginManager;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluationEvent;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluationListener;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluator;
import de.dfki.km.text20.services.evaluators.brain.BrainHandler;
import de.dfki.km.text20.services.evaluators.brain.BrainHandlerFlags;
import de.dfki.km.text20.services.evaluators.brain.options.AddBrainEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandler;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;

/**
 * An abstract {@link GazeHandler}, only used internally.
 * 
 * @author Ralf Biedert
 * @param <E>
 * @param <L>
 * @since 1.3
 */
public abstract class AbstractBrainHandler<E extends BrainEvaluationEvent, L extends BrainEvaluationListener<E>>
        implements BrainTrackingListener, BrainHandler {

    /** Related listener */
    protected L attachedListener;
    
    /** Options */
    protected AddBrainEvaluationListenerOption options[];

    /** The related gaze evaluator */
    protected BrainEvaluator brainEvaluator;

    /** Plugin manager */
    protected PluginManager pluginManager;

    /** */
    public AbstractBrainHandler() {}

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
     */
    public void init() {
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener#newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    @Override
    public void newTrackingEvent(final BrainTrackingEvent filteredEvent) {
        // Nothing to see here, move on.
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.gazeevaluator.GazeHandler#getFlags()
     */
    @Override
    public Collection<BrainHandlerFlags> getFlags() {
        return $.list();
    }
}
