/*
 * FixationHandler.java
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
package de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.fixation;

import java.awt.Point;
import java.util.List;

import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.util.InformationBrokerUtil;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.configuration.OptionFixationParametersItem;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionFixationParameters;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationUtil;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;

/**
 * @author Ralf Biedert
 */
public class FixationHandler extends AbstractGazeHandler {

    @SuppressWarnings("boxing")
    protected void handleEvent(final FixationEvent event) {
        if (this.reducedCommunication) return;

        final List<String> handler = this.masterGazeHandler.getHandlerForType("fixation");
        if (handler.size() == 0) return;

        // If the element is not visible, don't do anything
        if (!this.pseudorenderer.getStatus().contains(PseudorendererStatus.VISIBLE) || event.getType() == FixationEventType.FIXATION_CONTINUED)
            return;

        // Get arguments for the fixation listner
        final FixationUtil fu = new FixationUtil(event.getFixation());
        final Point p = this.pseudorenderer.convertPoint(event.getFixation().getCenter(), CoordinatesType.SCREEN_BASED, CoordinatesType.DOCUMENT_BASED);
        final String mode = event.getType().name().toUpperCase();
        final StringBuilder args = new StringBuilder();

        // In case we could not convert the fixation, don't do anything
        if (p == null) return;

        // Assemble optional arguments
        args.append("duration=" + fu.getFixationDuration());
        args.append(",meanderivation=" + fu.getMeanCenterDerivation());

        // Execute all handler
        for (final String h : handler) {
            this.browserPlugin.executeJSFunction(h, mode, p.x, p.y, args.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.services.mastergazehandler.impl.gazehandler.AbstractGazeHandler#
     * registerToEvaluator(de.dfki.km.augmentedtext.services.gazeevaluator.GazeEvaluator)
     */
    @Override
    protected void registerToEvaluator(final GazeEvaluator evaluator) {
        final InformationBrokerUtil broker = new InformationBrokerUtil(this.pluginManager.getPlugin(InformationBroker.class));
        final OptionFixationParameters fixationParameters = broker.get(OptionFixationParametersItem.class);
        
        // Create fixation listener
        evaluator.addEvaluationListener(new FixationListener() {
            @Override
            public void newEvaluationEvent(final FixationEvent event) {
                handleEvent(event);
            }
        }, fixationParameters);
    }
}
