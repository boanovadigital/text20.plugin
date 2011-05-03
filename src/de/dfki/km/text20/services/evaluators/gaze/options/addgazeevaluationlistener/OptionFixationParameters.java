/*
 * OptionFixationParameters.java
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
package de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.FixationListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;

/**
 * Specifies {@link Fixation} detection parameters to use. 
 * 
 * @author Ralf Biedert
 * @since 1.3
 * @see FixationListener
 */
public class OptionFixationParameters implements AddGazeEvaluationListenerOption {
    /** */
    private static final long serialVersionUID = 4718902145003522316L;

    /** */
    private final int minimalTime;

    /** */
    private int radiusFixationSize;

    /** */
    private int minFixationEvents = 3;

    /**
     * Constructs an options with the given radius and time.
     * 
     * @param radiusFixationSize The maximal radius in pixel to detect a fixation.
     * @param minimalTime The minimal time in ms to detect a fixation. 
     */
    public OptionFixationParameters(int radiusFixationSize, int minimalTime) {
        this(radiusFixationSize, minimalTime, 3);
    }

    /**
     * Constructs an options with the given radius, time and event count.
     * 
     * @param radiusFixationSize The maximal radius in pixel to detect a fixation.
     * @param minimalTime The minimal time in ms to detect a fixation. 
     * @param minFixationEvents The minimal number of events to require (<code>1</code> recommended).
     */
    public OptionFixationParameters(int radiusFixationSize, int minimalTime,
                                    int minFixationEvents) {
        this.minimalTime = minimalTime;
        this.radiusFixationSize = radiusFixationSize;
        this.minFixationEvents = minFixationEvents;
    }

    /**
     * @return the minimalTime
     */
    public int getMinimalTime() {
        return this.minimalTime;
    }

    /**
     * @return the radiusFixationSize
     */
    public int getRadiusFixationSize() {
        return this.radiusFixationSize;
    }

    /**
     * @param radiusFixationSize the radiusFixationSize to set
     */
    public void setRadiusFixationSize(int radiusFixationSize) {
        this.radiusFixationSize = radiusFixationSize;
    }

    /**
     * @return the minFixationEvents
     */
    public int getMinFixationEvents() {
        return this.minFixationEvents;
    }

}
