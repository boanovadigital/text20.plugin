/*
 * DisplacementRegion.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata;

import java.awt.Point;
import java.awt.Rectangle;

import org.simpleframework.xml.Element;

import de.dfki.km.text20.services.evaluators.gaze.util.filter.displacement.Displacer;

/**
 * Specifies how a region should be displaced.
 *
 * @author Ralf Biedert
 * @since 1.0
 */
public class DisplacementRegion implements Displacer {

    @Element
    Rectangle source;

    @Element
    Point displacement;

    /**
     * Creates an empty region.
     */
    public DisplacementRegion() {
        this.source = new Rectangle();
        this.displacement = new Point();
    }

    /**
     * Returns the source area that should be displaced.
     * 
     * @return The area.
     */
    public Rectangle getSource() {
        return this.source;
    }

    /**
     * Sets the source area.
     * 
     * @param source The source to displace.
     */
    public void setSource(Rectangle source) {
        this.source = source;
    }

    /**
     * Returns the displacement vector. 
     * 
     * @return the displacement The vector.
     */
    public Point getDisplacement() {
        return this.displacement;
    }

    /**
     * Sets the displacement vector.
     * 
     * @param displacement The displacement to set.
     */
    public void setDisplacement(Point displacement) {
        this.displacement = displacement;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.displacement.Displacer#displace(java.awt.Point)
     */
    @Override
    public Point displace(Point input) {
        if (this.source == null || this.displacement == null) return new Point(input);

        if (this.source.contains(input)) { return new Point(input.x + this.displacement.x, input.y + this.displacement.y); }

        return new Point(input);
    }
}