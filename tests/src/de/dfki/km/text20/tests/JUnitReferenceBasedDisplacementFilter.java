/*
 * JUnitReferenceBasedDisplacementFilter.java
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
package de.dfki.km.text20.tests;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.junit.Assert;
import org.junit.Test;

import de.dfki.km.text20.util.filter.displacement.ReferenceBasedDisplacementFilter;

/**
 * @author rb
 *
 */
public class JUnitReferenceBasedDisplacementFilter {

    /**
     * 
     */
    @Test
    public void testCalcDisplacement() {
        final ReferenceBasedDisplacementFilter filter = new ReferenceBasedDisplacementFilter();

        // so every reference point is accepted.
        filter.setRemovingRadius(0);

        // 1. Test two Vectors on same Line
        filter.updateReferencePoint(new Point(1, 1), 1, 1, 10);
        filter.updateReferencePoint(new Point(-1, -1), -1, -1, 14);

        Point res = filter.calcDisplacement(new Point(0, 0));
        Assert.assertEquals(res, new Point(0, 0));

        // 2. Test 4 vectors same distance & pairwise perpendicular vectors
        filter.updateReferencePoint(new Point(-1, 1), -2, 2, 15);
        filter.updateReferencePoint(new Point(1, -1), 2, -2, 20);

        res = filter.calcDisplacement(new Point(0, 0));
        Assert.assertEquals(res, new Point(0, 0));

        // 3. Test a Vector is on a reference point
        res = filter.calcDisplacement(new Point(-1, 1));
        Assert.assertEquals(res, new Point(-3, 3));

        // 4. Test calc a displacement....
        filter.clearReferencePoints();

        filter.updateReferencePoint(new Point(1, 3), 1, -1, 10);
        filter.updateReferencePoint(new Point(1, -1), 1, -4, 14);
        filter.updateReferencePoint(new Point(-3, 0), -2, 1, 16);

        res = filter.calcDisplacement(new Point(1, 0));

        // the resulting point should be in a circle with midpoint (2 -3.5) and radius 2
        // TODO: the model should be probably adopted....
        // ... because the resulting point is calculated at (1.84 -3.45)
        // the y-value is ok, but not the x value. since the second vector is the nearest
        // to the point and has no displacement to the x-axis. but the the displacement of
        // the point to the x direction is nearby 1. Which is quite big. 
        Assert.assertTrue(res.distance(new Point2D.Double(2, -3.5)) <= 2.);
    }

    /**
     * 
     */
    @Test
    public void testUpdateRefPoint() {
        final ReferenceBasedDisplacementFilter filter = new ReferenceBasedDisplacementFilter();
        Assert.assertTrue(filter.getRemovingRadius() == Math.sqrt(10000.0));

        filter.setRemovingRadius(10.);
        Assert.assertTrue(filter.getRemovingRadius() == Math.sqrt(Math.pow(10., 2)));

        // test refpoint added
        filter.updateReferencePoint(new Point(0, 0), -10, 5, 2);
        Point res = filter.calcDisplacement(new Point(0, 0));

        Assert.assertEquals(res, new Point(-10, 5));

        // add a refpoint much far away
        filter.updateReferencePoint(new Point(20, -15), -2, +3, 5);
        res = filter.calcDisplacement(new Point(20, -15));

        Assert.assertEquals(res, new Point(20 - 2, -15 + 3));

        // add a refpoint which is too close
        filter.updateReferencePoint(new Point(4, 3), -3, +2, 7);
        // (0,0) should be removed now...
        res = filter.calcDisplacement(new Point(0, 0));

        Assert.assertFalse(res.x == -10 && res.y == 5);

        // ... but (20,-15) should be stay
        res = filter.calcDisplacement(new Point(20, -15));

        Assert.assertEquals(res, new Point(20 - 2, -15 + 3));
    }

}
