/*
 * EvaluatorManager.java
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
import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.services.trackingdevices.common.TrackingDevice;
import de.dfki.km.text20.services.trackingdevices.common.TrackingDeviceInfo;
import de.dfki.km.text20.services.trackingdevices.common.TrackingEvent;
import de.dfki.km.text20.services.trackingdevices.common.TrackingListener;

/**
 * Creates {@link Evaluator}s of a given type for a given {@link TrackingDevice}.
 * 
 * @author Ralf Biedert
 * @param <T> The type of the {@link TrackingEvent}.
 * @param <U> The type of the {@link TrackingListener}.
 * @param <V> The type of the {@link Evaluator}.
 * @param <W> The type of the {@link TrackingDevice}.
 * @since 1.3
 */
public interface EvaluatorManager<T extends TrackingEvent, U extends TrackingListener<T>, V extends Evaluator<? extends EvaluationListener<? extends EvaluationEvent>, ? extends Option, ? extends Filter<T>>, W extends TrackingDevice<? extends TrackingDeviceInfo, T, U>>
        extends Plugin {
    /**
     * Constructs an evaluator for the given {@link TrackingDevice}.
     * 
     * @param trackingDevice The tracking device to create an evaluator for.
     * @return A new evaluator.
     */
    public V createEvaluator(W trackingDevice);
}
