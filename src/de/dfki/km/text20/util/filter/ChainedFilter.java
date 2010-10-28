/*
 * ChainedFilter.java
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
package de.dfki.km.text20.util.filter;

import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * Allows chaining of several filters and makes them execute sequentially.
 * 
 * @author Eugen Massini
 */
public class ChainedFilter extends AbstractFilter {
    private final List<AbstractFilter> filters = new ArrayList<AbstractFilter>();

    /**
     * Adds a filter to the chain.
     * 
     * @param filter
     */
    public void addFilter(final AbstractFilter filter) {
        this.filters.add(filter);
    }

    @Override
    public EyeTrackingEvent filterEvent(final EyeTrackingEvent event) {
        EyeTrackingEvent e = event; // use a new variable to suppress warning
        for (final AbstractFilter filter : this.filters) {
            e = filter.filterEvent(e);
        }
        return e;
    }
}
