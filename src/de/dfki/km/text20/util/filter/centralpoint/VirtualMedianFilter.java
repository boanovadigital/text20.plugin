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
import java.util.Arrays;

/**
 * 
 * Computes the center of the last n points, it now selects the point of the last n 
 * points which lies closest to that center.
 * 
 * @author rb
 *
 */
public class VirtualMedianFilter extends CentralPointFilter {

    /**
     * 
     * @param size
     */
    public VirtualMedianFilter(final int size) {
        super(size);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.centralpoint.CentralPointFilter#getPoint()
     */
    @Override
    Point getPoint() {
        if (this.lastPoints.size() < this.backlogSize) return null;

        final int x[] = new int[this.lastPoints.size()];
        final int y[] = new int[this.lastPoints.size()];

        int i = 0;

        for (final Point p : this.lastPoints) {
            x[i] = p.x;
            y[i] = p.y;
            i++;
        }

        Arrays.sort(x);
        Arrays.sort(y);

        final int s = this.lastPoints.size() / 2;

        return new Point(x[s], y[s]);
    }


}
