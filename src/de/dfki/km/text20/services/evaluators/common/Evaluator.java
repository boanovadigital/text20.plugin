/*
 * Evaluator.java
 * 
 * Copyright (c) 2010, Andre Hoffmann, DFKI. All rights reserved.
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
package de.dfki.km.text20.services.evaluators.common;

import net.xeoh.plugins.base.Option;
import de.dfki.km.text20.services.trackingdevices.common.TrackingDevice;
import de.dfki.km.text20.services.trackingdevices.common.TrackingEvent;

/**
 * The base evaluator for a given device. An evaluator usually takes raw data from 
 * a {@link TrackingDevice} and performs some aggregation/evaluation.   
 * 
 * @author Ralf Biedert
 * @param <T> The type of the {@link EvaluationListener}.
 * @param <U> The type of the {@link Option}.
 * @param <V> The type of the {@link Filter} 
 * @since 1.3
 */
public interface Evaluator<T extends EvaluationListener<? extends EvaluationEvent>, U extends Option, V extends Filter<? extends TrackingEvent>> {
    /**
     * Adds an evaluation listener.
     * 
     * @param listener The listener to add.
     * @param options The options to use.
     */
    public void addEvaluationListener(T listener, U... options);

    /**
     * Sets a filter this evaluator used to pre-process all incoming events.
     * 
     * @param filter The filter to use.
     */
    public void setFilter(V filter);
}
