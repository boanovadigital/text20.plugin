/*
 * GazeEvaluatorImpl.java
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

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import net.xeoh.plugins.base.PluginInformation;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.options.getplugin.OptionPluginSelector;
import net.xeoh.plugins.base.options.getplugin.PluginSelector;
import net.xeoh.plugins.base.util.OptionUtils;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluationListener;
import de.dfki.km.text20.services.evaluators.brain.BrainEvaluator;
import de.dfki.km.text20.services.evaluators.brain.BrainFilter;
import de.dfki.km.text20.services.evaluators.brain.BrainHandler;
import de.dfki.km.text20.services.evaluators.brain.BrainHandlerFactory;
import de.dfki.km.text20.services.evaluators.brain.BrainHandlerFlags;
import de.dfki.km.text20.services.evaluators.brain.options.AddBrainEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.brain.options.spawnevaluator.OptionBrainEvaluator;
import de.dfki.km.text20.services.evaluators.brain.options.spawnevaluator.OptionBrainEvaluatorPassthrough;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;

/**
 * @author Ralf Biedert
 */
public class BrainEvaluatorImpl implements BrainEvaluator, BrainTrackingListener {

    /** Locks this object for multiple concurrent brain entries. */
    private final ReentrantLock brainEventLock = new ReentrantLock();

    /** Used to pre-process all incoming brain data before it is provided to the handler */
    private BrainFilter brainFilter;

    /** Our plugin manager */
    private final PluginManager pluginManager;

    /** Plugin manager information class */
    final PluginInformation pluginInformation;

    /** List of our handlers */
    final List<BrainHandler> brainHandler = $.list();

    /**
     * Add ourselves to the tracking listener list.
     * 
     * @param gazeEvaluatorManagerImpl
     * 
     * @param trackingDevice
     */
    protected BrainEvaluatorImpl(final PluginManager pluginManager,
                                 final BrainTrackingDevice trackingDevice) {
        this.pluginManager = pluginManager;
        this.pluginInformation = pluginManager.getPlugin(PluginInformation.class);

        // Sanity check
        if (trackingDevice == null) {
            System.err.println("TrackingDevice is null. We won't do anything ... either you're debugging our you have a bug ...");
            return;
        }

        trackingDevice.addTrackingListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluator#addGazeEvaluationListener(de.dfki.km.
     * augmentedtext.sandbox.services.gazeevaluator.GazeEvaluationListener)
     */
    @Override
    public void addEvaluationListener(final BrainEvaluationListener<?> listener,
                                      AddBrainEvaluationListenerOption... _options) {

        // First check our prerequisites
        final AddBrainEvaluationListenerOption options[] = $(_options).compact().array(AddBrainEvaluationListenerOption.class);
        final OptionUtils<AddBrainEvaluationListenerOption> ou = new OptionUtils<AddBrainEvaluationListenerOption>(options);

        // Select the proper plugin
        final BrainHandlerFactory selectedCreator = this.pluginManager.getPlugin(BrainHandlerFactory.class, new OptionPluginSelector<BrainHandlerFactory>(new PluginSelector<BrainHandlerFactory>() {
            @Override
            public boolean selectPlugin(BrainHandlerFactory creator) {
                if (!creator.getEvaluatorType().isAssignableFrom(listener.getClass()))
                    return false;

                return true;
            }
        }));

        // Try to spawn the evaluator ...
        final BrainHandler spawnEvaluator = selectedCreator.spawnEvaluator(listener, new OptionBrainEvaluatorPassthrough(options), new OptionBrainEvaluator(this));
        if (spawnEvaluator == null) { throw new IllegalStateException("Unable to spawn the selected evaluator. This is a bug."); }

        // ... and register it.
        try {
            this.brainEventLock.lock();
            this.brainHandler.add(spawnEvaluator);
        } finally {
            this.brainEventLock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener#newTrackingEvent(de.dfki.km.augmentedtext.
     * services.trackingdevices.TrackingEvent)
     */
    @Override
    public void newTrackingEvent(final BrainTrackingEvent event) {
        BrainTrackingEvent filteredEvent = event;

        // Sanity check
        if (event == null) return;

        // We ignore invalid points. NO WE DON'T!!!!!
        // if (!event.areValid(EyeTrackingEventValidity.CENTER_POSITION_VALID)) return;

        // Check for event flooding.
        if (this.brainEventLock.isLocked()) {
            System.err.println("Event flooding detected. This means handling takes too long, events arrive too fast, or both.");
        }

        // Process all known handler
        try {
            this.brainEventLock.lock();

            // Filter the event if possible.
            if (this.brainFilter != null) {
                filteredEvent = this.brainFilter.filterEvent(event);
            }

            // Process every handler
            for (final BrainHandler handler : this.brainHandler) {

                // Check if the handler requires unfiltered events or filtered ones.
                if (handler.getFlags().contains(BrainHandlerFlags.REQUIRE_UNFILTERED)) {
                    handler.newTrackingEvent(event);
                } else {
                    handler.newTrackingEvent(filteredEvent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.brainEventLock.unlock();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.text20.services.evaluators.common.Evaluator#setFilter(de.dfki.km.text20.services.evaluators.common
     * .Filter)
     */
    @Override
    public void setFilter(final BrainFilter brainFilter) {
        this.brainFilter = brainFilter;
    }
}
