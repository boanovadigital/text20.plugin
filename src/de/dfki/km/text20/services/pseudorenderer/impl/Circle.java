/*
 * Circle.java
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
package de.dfki.km.text20.services.pseudorenderer.impl;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author rb
 */
final class Circle {

    /** */
    private final Point center;

    /** */
    private final int radius;

    /**
     * @param center
     * @param radius
     */
    public Circle(Point center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Little imprecision: We cut the edges of the rectangle with our circle, thus we do not fully enclose it. 
     * 
     * @param rectangle
     */
    public Circle(Rectangle rectangle) {
        this.center = new Point((int) rectangle.getCenterX(), (int) rectangle.getCenterY());

        // Set radius to the larger of the two.
        this.radius = Math.max(rectangle.height, rectangle.width) / 2;
    }

    /**
     * Calulates the distance of a point from the border of the circle. 
     * 
     * @param point
     * @return . 
     */
    public double borderDistance(Point point) {
        return this.center.distance(point) - this.radius;
    }
}
