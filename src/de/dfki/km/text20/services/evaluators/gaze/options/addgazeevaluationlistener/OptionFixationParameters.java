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

import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;

/**
 * 
 * @author Ralf Biedert
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
     * @param radiusFixationSize 
     * @param minimalTime
     */
    public OptionFixationParameters(int radiusFixationSize, int minimalTime) {
        this(radiusFixationSize, minimalTime, 3);
    }

    /**
     * @param radiusFixationSize 
     * @param minimalTime
     * @param minFixationEvents 
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
