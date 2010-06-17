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
package de.dfki.km.text20.services.evaluators.gaze.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import net.xeoh.plugins.base.PluginInformation;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.PluginInformation.Information;
import net.xeoh.plugins.base.options.getplugin.OptionPluginSelector;
import net.xeoh.plugins.base.options.getplugin.PluginSelector;
import net.xeoh.plugins.base.util.OptionUtils;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.GazeFilter;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.GazeHandlerFlags;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionRequestVersion;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.options.spawnevaluator.OptionGazeEvaluatorPassthrough;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;

/**
 * @author rb
 *
 */
public class GazeEvaluatorImpl implements GazeEvaluator, EyeTrackingListener {

    /** Locks this object for multiple concurrent gaze entries. */
    private final ReentrantLock gazeEventLock = new ReentrantLock();

    /** Used to pre-process all incoming gaze data before it is provided to the handler */
    private GazeFilter gazeFilter;

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    private final PluginManager pluginManager;

    /** */
    final PluginInformation pluginInformation;

    /** List of our handlers */
    final List<GazeHandler> gazeHandler = new ArrayList<GazeHandler>();

    /**
     * Add ourselves to the tracking listener list. 
     * @param gazeEvaluatorManagerImpl 
     * 
     * @param trackingDevice
     */
    protected GazeEvaluatorImpl(final PluginManager pluginManager,
                                final EyeTrackingDevice trackingDevice) {
        this.pluginManager = pluginManager;
        this.pluginInformation = pluginManager.getPlugin(PluginInformation.class);

        // Sanity check
        if (trackingDevice == null) {
            this.logger.warning("TrackingDevice is null. We won't do anything ... either you're debugging our you have a bug ...");
            return;
        }

        trackingDevice.addTrackingListener(this);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluator#addGazeEvaluationListener(de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluationListener)
     */
    public void addGazeEvaluationListener(final GazeEvaluationListener<?> listener,
                                          AddGazeEvaluationListenerOption... options) {

        final OptionUtils<AddGazeEvaluationListenerOption> ou = new OptionUtils<AddGazeEvaluationListenerOption>(options);
        final Collection<OptionRequestVersion> requestedVersions = ou.getAll(OptionRequestVersion.class);

        GazeEvaluatorImpl.this.logger.fine("Obtained request to return an evaluator for " + listener.getClass());

        // Select the proper plugin
        final GazeHandlerFactory selectedCreator = this.pluginManager.getPlugin(GazeHandlerFactory.class, new OptionPluginSelector<GazeHandlerFactory>(new PluginSelector<GazeHandlerFactory>() {

            @Override
            public boolean selectPlugin(GazeHandlerFactory creator) {

                GazeEvaluatorImpl.this.logger.finer("Examining plugin factory " + creator);

                final PluginInformation pi = GazeEvaluatorImpl.this.pluginInformation;

                if (!creator.getEvaluatorType().isAssignableFrom(listener.getClass()))
                    return false;

                GazeEvaluatorImpl.this.logger.finer("Plugin looks promising");

                // Now check the requested versions
                for (OptionRequestVersion rqv : requestedVersions) {
                    // Check if we're dealing with the proper listener.
                    if (!rqv.getListener().isAssignableFrom(listener.getClass()))
                        continue;

                    GazeEvaluatorImpl.this.logger.finer("Found a version request. Checking authors ... ");

                    // Check author
                    if (rqv.getAuthor() != null) {
                        if (!pi.getInformation(Information.AUTHORS, creator).contains(rqv.getAuthor()))
                            return false;
                    }

                    GazeEvaluatorImpl.this.logger.finer("Checking implementaiton version ... ");

                    // Check version
                    if (rqv.getVersion() >= 0) {
                        final Collection<String> information = pi.getInformation(Information.VERSION, creator);
                        if (information.size() != 1) return false;

                        final int version = Integer.parseInt(information.iterator().next());

                        // We accept any newer version
                        if (version < rqv.getVersion()) return false;
                    }

                    GazeEvaluatorImpl.this.logger.finer("Checking capabilities ... ");

                    // Check capabilities
                    if (rqv.getCapabilities().length > 0) {
                        final Collection<String> information = pi.getInformation(Information.CAPABILITIES, creator);

                        // Must match all caps
                        if (!information.containsAll(Arrays.asList(rqv.getCapabilities())))
                            return false;
                    }
                }

                GazeEvaluatorImpl.this.logger.finer("All tests passed. Plugin selected.");

                return true;
            }
        }));

        // No handler to spawn something found, tthis is bad
        if (selectedCreator == null) {
            this.logger.warning("No handler found for requested listener " + listener.getClass().getInterfaces()[0]);
            return;
        }

        final GazeHandler spawnEvaluator = selectedCreator.spawnEvaluator(listener, new OptionGazeEvaluatorPassthrough(options), new OptionGazeEvaluator(this));

        // Even worse: the handler was unable to spawn something
        if (spawnEvaluator == null) { throw new IllegalStateException("Unable to spawn the selected evaluator. This is a bug."); }

        // Register the evaluator
        try {
            this.gazeEventLock.lock();
            this.gazeHandler.add(spawnEvaluator);
        } finally {
            this.gazeEventLock.unlock();
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingListener#newTrackingEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */
    public void newTrackingEvent(final EyeTrackingEvent event) {
        EyeTrackingEvent filteredEvent = event;

        // Sanity check
        if (event == null) return;

        // We ignore invalid points.
        if (!event.areValid(EyeTrackingEventValidity.CENTER_POSITION_VALID)) return;

        // Check for event flooding.
        if (this.gazeEventLock.isLocked()) {
            this.logger.warning("Event flooding detected. This means handling takes too long, events arrive too fast, or both.");
        }

        // Process all known handler
        try {
            this.gazeEventLock.lock();

            // Filter the event if possible.
            if (this.gazeFilter != null) {
                filteredEvent = this.gazeFilter.filterEvent(event);
            }

            // Process every handler
            for (final GazeHandler handler : this.gazeHandler) {

                // Check if the handler requires unfiltered events or filtered ones.
                if (handler.getFlags().contains(GazeHandlerFlags.REQUIRE_UNFILTERED)) {
                    handler.newTrackingEvent(event);
                } else {
                    handler.newTrackingEvent(filteredEvent);
                }
            }
        } catch (Exception e) {
            this.logger.warning("Exception occured during event handling. You should check the stack trace.");
            e.printStackTrace();
        } finally {
            this.gazeEventLock.unlock();
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeEvaluator#setGazeFilter(de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.GazeFilter)
     */
    public void setGazeFilter(final GazeFilter gazeFilter) {
        this.gazeFilter = gazeFilter;
    }

}
