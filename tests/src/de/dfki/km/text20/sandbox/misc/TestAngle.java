/*
 * TestAngle.java
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
package de.dfki.km.text20.sandbox.misc;

import java.awt.Point;
import java.util.HashMap;

/**
 * @author Ralf Biedert
 *
 */
public class TestAngle {

    /**
     * @param args
     */
    @SuppressWarnings("boxing")
    public static void main(final String[] args) {
        final Point c = new Point(0, 0);
        final Point c1 = new Point(0, 100);
        final Point c2 = new Point(-100, 0);
        final Point c3 = new Point(0, -100);
        final Point c4 = new Point(100, 0);

        final double distance = c.distance(c2);

        System.out.println(Math.atan2(c1.getY() - c.getY(), c1.getX() - c.getX()));
        System.out.println(Math.atan2(c2.getY() - c.getY(), c2.getX() - c.getX()));
        System.out.println(Math.atan2(c3.getY() - c.getY(), c3.getX() - c.getX()));
        System.out.println(Math.atan2(c4.getY() - c.getY(), c4.getX() - c.getX()));

        System.out.println(Math.asin((c1.getY() - c.getY()) / distance));
        System.out.println(Math.asin((c2.getY() - c.getY()) / distance));
        System.out.println(Math.asin((c3.getY() - c.getY()) / distance));
        System.out.println(Math.asin((c4.getY() - c.getY()) / distance));

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int x = map.get("S");
        System.out.println(x);
    }
}
