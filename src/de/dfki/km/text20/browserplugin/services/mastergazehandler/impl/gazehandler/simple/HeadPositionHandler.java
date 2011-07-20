/*
 * HeadPositionHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.simple;

import java.util.List;

import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.raw.RawGazeListener;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Ralf Biedert
 *
 */
public class HeadPositionHandler extends AbstractGazeHandler {

    private int i = 0;

    @SuppressWarnings("boxing")
    protected void handleEvent(final EyeTrackingEvent event) {
        if (this.reducedCommunication) return;

        // Execute rawGaze handler
        final List<String> handler = this.masterGazeHandler.getHandlerForType("headPosition");
        float[] headPosition = event.getHeadPosition();

        // Call only every 2nd turn (improves performance)
        if (this.i++ % 2 == 0) return;

        for (final String h : handler) {
            this.browserPlugin.executeJSFunction(h, event.getObservationTime(), headPosition[0], headPosition[1], headPosition[2]);
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler#registerToEvaluator(de.dfki.km.augmentedtext.services.gazeevaluator.GazeEvaluator)
     */

    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {
        evaluator.addEvaluationListener(new RawGazeListener() {

            @Override
            public void newEvaluationEvent(final RawGazeEvent event) {
                handleEvent(event.getTrackingEvent());
            }

            @Override
            public boolean requireUnfilteredEvents() {
                return false;
            }
        });
    }
}
