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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.weaksaccade.v2;

import java.util.logging.Logger;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.SaccadeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.WeakSaccadeListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionFixationParameters;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationUtil;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;

/**
 *
 * Weak Saccade Handler.
 *
 * @author Ralf Biedert
 */
public class WeakSaccadeHandler2 extends
        AbstractGazeHandler<SaccadeEvent, WeakSaccadeListener> {

    /** */
    @SuppressWarnings("unused")
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /** */
    private Fixation lastFixation;

    /**
     * @param listener
     * @param options
     */
    public WeakSaccadeHandler2(WeakSaccadeListener listener,
                               AddGazeEvaluationListenerOption... options) {
        super(listener);

    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.impl.AbstractGazeHandler#init(de.dfki.km.augmentedtext.services.gazeevaluator.options.AddGazeEvaluationListenerOption[])
     */
    @Override
    public void init(AddGazeEvaluationListenerOption... options) {

        this.gazeEvaluator.addEvaluationListener(new FixationListener() {

            @Override
            public void newEvaluationEvent(final FixationEvent event) {
                newGazeEvent(event);
            }

        }, new OptionFixationParameters(30, 25, 2));
    }

    /**
     * @param event
     */
    void newGazeEvent(FixationEvent event) {

        // In case we have a fixation ended, memorize its position,
        if (event.getType() == FixationEventType.FIXATION_END) {
            this.lastFixation = event.getFixation();
        }

        // In case we have a new fixation, and an old one
        if (event.getType() == FixationEventType.FIXATION_START && this.lastFixation != null) {

            final Fixation finalLastFixation = this.lastFixation;
            final Fixation currentFixation = event.getFixation();

            final Saccade saccade = new Saccade() {

                @Override
                public Fixation getStart() {
                    return finalLastFixation;
                }

                @Override
                public Fixation getEnd() {
                    return currentFixation;
                }
            };

            callListener(new SaccadeEvent() {

                @Override
                public Saccade getSaccade() {
                    return saccade;
                }

                @Override
                public long getGenerationTime() {
                    return new FixationUtil(currentFixation).getEndDate().getTime();
                }
            });
        }
    }
}
