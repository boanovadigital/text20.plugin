/*
 * LowPassFilter.java
 * 
 * Copyright (c) 2010, Eugen Massini, DFKI. All rights reserved.
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
package de.dfki.km.text20.util.filter.etufilters;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Eugen Massini
 */
public final class LowPassFilter {
    /**
     * Apply a Low-pass filter on source. Resulting is in @a dest.
     * @param <T> 
     * 
     * @param dest Contains filtered Points. Should be an array of same size as source. 
     * @param source
     * @param dt
     * @param rc
     */
    public static final <T extends Point2D> void apply(final List<T> dest,
                                                       final List<T> source,
                                                       final double dt, final double rc) {

        final double alpha = dt / (rc + dt);

        assert !source.isEmpty();
        assert dest.size() == source.size();

        dest.get(0).setLocation(source.get(0));

        for (int i = 1; i < source.size(); ++i) {
            dest.get(i).setLocation(dest.get(i - 1).getX() + alpha * (source.get(i).getX() - dest.get(i - 1).getX()), dest.get(i - 1).getY() + alpha * (source.get(i).getY() - dest.get(i - 1).getY()));
        }
    }
}
