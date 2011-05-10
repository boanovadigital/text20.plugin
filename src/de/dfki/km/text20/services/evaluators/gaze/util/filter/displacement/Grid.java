/*
 * Grid.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.filter.displacement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflects a displacement-grid. 
 * 
 * @author rb
 */
public class Grid {

    /** */
    private final int xresolution;

    /** */
    private final int yresolution;

    /** */
    List<List<GridEntry>> grids = new ArrayList<List<GridEntry>>();

    /**
     * Construct grid with given resolution.
     * 
     * @param xresolution
     * @param yresolution
     */
    public Grid(final int xresolution, final int yresolution) {
        this.xresolution = xresolution;
        this.yresolution = yresolution;

        // TODO: Make this dynamically, or even create at getGrid()...
        for (int i = 0; i < 3000 / yresolution; i++) {
            final List<GridEntry> container = new ArrayList<GridEntry>();

            for (int j = 0; j < 3000 / xresolution; j++) {
                container.add(new GridEntry());
            }

            this.grids.add(container);
        }
    }

    /**
     * Returns the appropriate grid entry for a point.
     * 
     * @param p
     * @return .
     */
    public GridEntry getGridForPoint(final Point p) {
        final int i = p.x / this.xresolution;
        final int j = p.y / this.yresolution;

        return this.grids.get(j).get(i);
    }
}
