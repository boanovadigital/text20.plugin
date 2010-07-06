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
package de.dfki.km.text20.services.evaluators.gaze.impl.handler.fixationline.v4;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.impl.elements.FixationLineImpl;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEvent;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineEventType;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLineListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationLineUtil;
import de.dfki.km.text20.services.evaluators.gaze.util.handler.AbstractGazeHandler;

/**
 * 
 * @author rb
 */
public class FixationLineHandler4 extends
        AbstractGazeHandler<FixationLineEvent, FixationLineListener> {

    private int misdetected;

    /** If set we have a fixation line the user follows. */
    FixationLineImpl currentFixationLine;

    long currentTime = 0;

    long avgStartLength = 0;

    /** */
    final List<Fixation> lastFixations = new ArrayList<Fixation>();

    /**
     * @param listener
     * @param options 
     */
    public FixationLineHandler4(final FixationLineListener listener,
                                AddGazeEvaluationListenerOption... options) {
        super(listener);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.impl.AbstractGazeHandler#init()
     */

    /**
     * Returns the last n fixations.
     * @param n
     * @return .
     */
    public List<Fixation> getLast(final int n) {
        final int s = this.lastFixations.size();
        return new ArrayList<Fixation>(this.lastFixations.subList(s - n, s));
    }

    @Override
    public void init(AddGazeEvaluationListenerOption... options) {
        this.gazeEvaluator.addEvaluationListener(new FixationListener() {

            public void newEvaluationEvent(final FixationEvent event) {
                if (event.getType().equals(FixationEventType.FIXATION_END)) {
                    FixationLineHandler4.this.currentTime = event.getGenerationTime();
                    nextFixation(event.getFixation());
                }
            }
        }, options);
    }

    /**
     * @param fixations
     * @return
     */
    private FixationLineImpl createFixationLine(final List<Fixation> fixations) {
        return new FixationLineImpl(fixations);
    }

    /**
     * Dispatches the new event to the listener.
     * 
     * @param type
     */
    private void dispatch(final FixationLineEventType type) {
        final FixationLine line = this.currentFixationLine;

        final long time = FixationLineHandler4.this.currentTime;

        callListener(new FixationLineEvent() {

            public FixationLineEventType getEventType() {
                return type;
            }

            public FixationLine getFixationLine() {
                return line;
            }

            public long getGenerationTime() {
                return time;
            }
        });
    }

    /**
     * @param current
     */
    void nextFixation(final Fixation current) {

        this.lastFixations.add(current);

        // Keep size constant.
        if (this.lastFixations.size() > 50) {
            this.lastFixations.remove(0);
        }

        // Need a minimum size
        if (this.lastFixations.size() < 3) return;

        // Check if we should look for a new fixation line ...
        if (this.currentFixationLine == null) {
            final FixationLineImpl candidata = createFixationLine(getLast(3));
            final FixationLineUtil flu = new FixationLineUtil(candidata);

            // Check start of line.
            for (double d : flu.getAllAngles()) {
                if (Math.abs(d) > 0.4) return;
            }
            // if (flu.getDimension().width > 100) return;
            // if (flu.getAvgVerticalDeviation() > 50) return;

            this.currentFixationLine = candidata;
            this.avgStartLength = flu.getAvgSaccadeLength();

            dispatch(FixationLineEventType.FIXATION_LINE_STARTED);
            this.misdetected = 0;
        } else {
            // ... or for a continuation
            final FixationLineUtil flu = new FixationLineUtil(this.currentFixationLine);
            final Fixation lastProperFixation = flu.getLastFixations(1).get(0);

            final Point properCenter = lastProperFixation.getCenter();
            final Point currentCenter = getLast(1).get(0).getCenter();

            final int oldMis = this.misdetected;

            // Check backward jump distance
            if (currentCenter.x - properCenter.x < -2 * this.avgStartLength) {
                this.misdetected++;
            }

            // Check forward jump distance
            if (currentCenter.x - properCenter.x > 2.5 * this.avgStartLength) {
                this.misdetected++;
            }

            // Check angle (TODO...)
            if (Math.abs(flu.getAverageYPosition() - currentCenter.y) > 50) {
                this.misdetected++;
            }

            // Check current status and dispatch
            if (oldMis < this.misdetected) {
                if (this.misdetected < 2) return;

                dispatch(FixationLineEventType.FIXATION_LINE_ENDED);

                this.misdetected = 0;
                this.currentFixationLine = null;
            } else {
                this.currentFixationLine.addFixation(current);
                this.misdetected = 0;

                dispatch(FixationLineEventType.FIXATION_LINE_CONTINUED);
            }
        }
    }
}
