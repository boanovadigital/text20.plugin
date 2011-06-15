/*
 * FixationFilter.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.filter.fixation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.util.filter.AbstractFilter;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventWrapper;

/**
 * @author rb
 *
 */
public class FixationFilter extends AbstractFilter {

    /** Size (diameter) of the inner ring */
    public static final int INNER_RING_SIZE = 30;

    /** Size (diameter) of the outer ring */
    public static final int OUTER_RING_SIZE = 50;

    /** Number of elements close to accept a fixation */
    private static final int FIXATION_THRESHOLD = 3;

    /** Size of the queue */
    private static final int QUEUE_SIZE = 10;

    boolean endOfFixation = false;

    /** Do we have a fixation somewhere */
    Point fixation = new Point(0, 0);

    /** Last observed points */
    List<EyeTrackingEvent> lastPoints = new ArrayList<EyeTrackingEvent>();

    boolean newFixation = false;

    /** Counts how many elements had been outside the outer ring since last counter reset */
    int numOutsideOuterRing = 0;

    boolean stillInsideFixation = false;

    @Override
    public EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {

        // Reset flags every round
        this.newFixation = false;
        this.endOfFixation = false;
        this.stillInsideFixation = false;

        // Add a new entry
        addEntry(event);

        // Check our size constraints hold
        assertSize();

        // Checks and updates some conditions.
        checkStillInsideFixation();
        checkNewFixation();
        checkLastFixationStopped();

        // If we have a fixation, return it.
        if (this.fixation != null) {
            final Point f = this.fixation;

            return new EyeTrackingEventWrapper(event) {

                @Override
                public Point getGazeCenter() {
                    return f;
                }
            };
        }

        return event;
    }

    /**
     * Returns the latest fixation, or null if there is currently none
     * @return .
     */
    public Fixation getFixation() {

        final Point center = this.fixation;

        return new Fixation() {

            @Override
            public Point getCenter() {
                return center;
            }

            @Override
            public List<EyeTrackingEvent> getTrackingEvents() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    /**
     * @return the endOfFixation
     */
    public boolean isEndOfFixation() {
        return this.endOfFixation;
    }

    /**
     * @return the newFixation
     */
    public boolean isNewFixation() {
        return this.newFixation;
    }

    /**
     * @return the stillInsideFixation
     */
    public boolean isStillInsideFixation() {
        return this.stillInsideFixation;
    }

    /**
     * @param t
     */
    private void addEntry(final EyeTrackingEvent t) {
        this.lastPoints.add(t);
    }

    /**
     * Tries to assure a certain size.
     *
     * @return
     */
    private boolean assertSize() {
        if (this.lastPoints.size() < QUEUE_SIZE) return false;
        this.lastPoints.remove(0);
        return true;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.AbstractFilter#filterEvent(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent)
     */

    /**
     * Check if the last fixation has stopped
     *
     * @return .
     */
    private boolean checkLastFixationStopped() {
        if (this.fixation == null) return false;
        final EyeTrackingEvent last = getLast(1).get(0);

        if (last.getGazeCenter().distance(this.fixation) > OUTER_RING_SIZE / 2) {
            this.numOutsideOuterRing++;
        }
        if (last.getGazeCenter().distance(this.fixation) <= INNER_RING_SIZE / 2) {
            this.numOutsideOuterRing = 0;
        }

        if (this.numOutsideOuterRing > FIXATION_THRESHOLD) {
            this.fixation = null;
            this.endOfFixation = true;
            return true;
        }

        return false;
    }

    /**
     * Check if we have a new fixation
     *
     * @return .
     */
    private boolean checkNewFixation() {
        if (this.fixation != null) return false;
        final List<EyeTrackingEvent> last = getLast(FIXATION_THRESHOLD);

        // Compute middle
        final Point avg = new Point();

        for (final EyeTrackingEvent entry : last) {
            avg.x += entry.getGazeCenter().x;
            avg.y += entry.getGazeCenter().y;
        }

        avg.x /= last.size();
        avg.y /= last.size();

        // Compute distance
        for (final EyeTrackingEvent entry : last) {
            if (entry.getGazeCenter().distance(avg) > INNER_RING_SIZE / 2) return false;
        }

        this.fixation = avg;
        this.newFixation = true;

        return true;

    }

    /**
     * Check if the gaze is fixating
     *
     * @return .
     */
    private boolean checkStillInsideFixation() {
        if (!assertSize()) return false;
        if (this.fixation == null) return false;
        final EyeTrackingEvent last = getLast(1).get(0);

        if (last.getGazeCenter().distance(this.fixation) <= INNER_RING_SIZE / 2) {
            this.stillInsideFixation = true;
            return true;
        }

        return false;
    }

    /**
     * Returns the last n events.
     *
     * @param n
     * @return
     */
    private List<EyeTrackingEvent> getLast(final int n) {
        final int size = this.lastPoints.size();
        return this.lastPoints.subList(size - n, size);

    }
}
