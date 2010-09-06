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
package de.dfki.km.text20.util.filter.centralpoint;

import java.awt.Point;

/**
 * 
 * Computes the center of the last n points, it now selects the point of the last n points which lies closest to that center.
 * 
 * @author rb
 *
 */
public class MedianFilter extends CentralPointFilter {

    /**
     * 
     * @param size
     */
    public MedianFilter(final int size) {
        super(size);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.centralpoint.CentralPointFilter#getPoint()
     */

    @Override
    Point getPoint() {
        final Point center = new Point();

        for (final Point p : this.lastPoints) {
            center.x += p.x;
            center.y += p.y;
        }

        center.x /= this.lastPoints.size();
        center.y /= this.lastPoints.size();

        Point rval = null;
        double dist = Double.MAX_VALUE;

        for (final Point p : this.lastPoints) {
            final double dd = p.distance(center);
            if (dd < dist) {
                rval = p;
                dist = dd;
            }
        }

        return rval;
    }
}
