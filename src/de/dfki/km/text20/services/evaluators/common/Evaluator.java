/*
 * Evaluator.java
 * 
 * Copyright (c) 2010, André Hoffmann, DFKI. All rights reserved.
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
import de.dfki.km.text20.services.trackingdevices.common.TrackingEvent;

/**
 * A evaluator bound to a specific device. Different listeners may be registered using the method below.
 * 
 * @author rb
 */
public interface Evaluator<T extends EvaluationListener<? extends EvaluationEvent>, U extends Option, V extends Filter<? extends TrackingEvent>> {

    /**
     * @param listener
     * @param options 
     */
    public void addEvaluationListener(T listener, U... options);

    /**
     * Sets a filter this evaluator used to pre-process all incoming event.
     * 
     * @param filter
     */
    public void setFilter(V filter);
}
