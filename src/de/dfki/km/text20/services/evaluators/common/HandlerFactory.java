/*
 * HandlerFactory.java
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
 */package de.dfki.km.text20.services.evaluators.common;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.services.evaluators.common.options.SpawnEvaluatorOption;
import de.dfki.km.text20.services.trackingdevices.common.TrackingEvent;

public interface HandlerFactory<T extends EvaluationListener<? extends EvaluationEvent>, U extends Handler<? extends TrackingEvent>>
        extends Plugin {

    /**
     * Returns a list with all supported evaluators
     * 
     * @return .
     */
    public Class<? extends T> getEvaluatorType();

    /**
     * @param listener 
     * @param options 
     * @return .
     */
    public U spawnEvaluator(T listener, SpawnEvaluatorOption... options);
}
