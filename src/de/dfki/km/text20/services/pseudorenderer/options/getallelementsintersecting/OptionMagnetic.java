/*
 * OptionMagnetic.java
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
package de.dfki.km.text20.services.pseudorenderer.options.getallelementsintersecting;

import de.dfki.km.text20.services.pseudorenderer.options.GetAllElementsIntersectingOption;

/**
 * If specified, the selection works magnetic. In that case at most one element is 
 * returned, namely the closest one. In case there is no element within a sensible range, 
 * also no element might be returned.
 * 
 * @author rb
 */
public class OptionMagnetic implements GetAllElementsIntersectingOption {
    /**  */
    private static final long serialVersionUID = -6749545391594029171L;

    /** */
    private final int maxDistance;

    /**  */
    public OptionMagnetic() {
        this(Integer.MAX_VALUE);
    }

    /**
     * @param maxValue Elements further away than that are not accepted.
     */
    public OptionMagnetic(int maxValue) {
        this.maxDistance = maxValue;
    }

    /**
     * @return the maxDistance
     */
    public int getMaxDistance() {
        return this.maxDistance;
    }
}
