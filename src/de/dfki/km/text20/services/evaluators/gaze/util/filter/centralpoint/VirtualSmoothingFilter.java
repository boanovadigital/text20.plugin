/*
 * VirtualSmoothingFilter.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.filter.centralpoint;

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
public class VirtualSmoothingFilter extends CentralPointFilter {

    final int xs;
    final int ys;

    /**
     * 
     * @param xsize
     * @param ysize
     */
    public VirtualSmoothingFilter(final int xsize, final int ysize) {
        super(Math.max(xsize, ysize));

        this.xs = xsize;
        this.ys = ysize;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.centralpoint.CentralPointFilter#getPoint()
     */
    @Override
    Point getPoint() {
        if (this.lastPoints.size() < this.backlogSize) return null;

        final int x[] = new int[this.xs];
        final int y[] = new int[this.ys];

        for (int i = 0; i < this.xs; i++) {
            x[i] = this.lastPoints.get(this.backlogSize - 1 - i).x;
        }

        for (int i = 0; i < this.ys; i++) {
            y[i] = this.lastPoints.get(this.backlogSize - 1 - i).y;
        }

        Arrays.sort(x);

        int my = 0;
        for (int i : y) {
            my += i;
        }
        my /= y.length;

        final int s = this.lastPoints.size() / 2;

        return new Point(x[s], my);
    }

}
