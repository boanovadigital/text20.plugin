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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.saccade.v1;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.SaccadeEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.SaccadeListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;

/**
 * 
 * @author rb
 */
public class SaccadeHandler extends AbstractGazeHandler<SaccadeEvent, SaccadeListener> {

    /** Last fixation */
    Fixation lastFixation;

    /**
     * @param listener
     * @param options 
     */
    public SaccadeHandler(final SaccadeListener listener,
                          AddGazeEvaluationListenerOption... options) {
        super(listener);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.impl.AbstractGazeHandler#init()
     */

    @Override
    public void init(AddGazeEvaluationListenerOption... options) {
        this.gazeEvaluator.addGazeEvaluationListener(new FixationListener() {

            public void newGazeEvaluationEvent(final FixationEvent event) {
                if (event.getType().equals(FixationEventType.FIXATION_START)) {
                    nextFixation(event.getGenerationTime(), event.getFixation());
                }
            }
        }, options);
    }

    /**
     * @param l 
     * @param current
     */
    void nextFixation(final long l, final Fixation current) {
        // In case we only had one fixation, don't do anything.                
        if (this.lastFixation == null) {
            this.lastFixation = current;
            return;
        }

        // TODO: Proper handling of non-saccades between two fixations, i.e. when the time and or distance between two 
        // fixation was too long, you wouldn't call it a saccade anymore.

        final Fixation last = this.lastFixation;

        callListener(new SaccadeEvent() {

            public long getGenerationTime() {
                return l;
            }

            public Saccade getSaccade() {
                return new Saccade() {

                    public Fixation getEnd() {
                        return current;
                    }

                    public Fixation getStart() {
                        return last;
                    }
                };
            }
        });
    }
}
