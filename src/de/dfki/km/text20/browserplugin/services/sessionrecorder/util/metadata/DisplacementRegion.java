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

import de.dfki.km.text20.util.filter.displacement.Displacer;

/**
 * Specifies how a region has to be displaced
 * 
 * @author Ralf Biedert
 *
 */
public class DisplacementRegion implements Displacer {

    @Element
    Rectangle source;

    @Element
    Point displacement;

    /**
     * Creates an empty region
     */
    public DisplacementRegion() {
        this.source = new Rectangle();
        this.displacement = new Point();
    }

    /**
     * @return the source
     */
    public Rectangle getSource() {
        return this.source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Rectangle source) {
        this.source = source;
    }

    /**
     * @return the displacement
     */
    public Point getDisplacement() {
        return this.displacement;
    }

    /**
     * @param displacement the displacement to set
     */
    public void setDisplacement(Point displacement) {
        this.displacement = displacement;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.filter.displacement.Displacer#displace(java.awt.Point)
     */
    public Point displace(Point input) {
        if (this.source == null || this.displacement == null) return new Point(input);

        if (this.source.contains(input)) { return new Point(input.x + this.displacement.x, input.y + this.displacement.y); }

        return new Point(input);
    }
}