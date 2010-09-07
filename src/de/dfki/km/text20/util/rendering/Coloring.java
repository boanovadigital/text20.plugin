/*
 * Coloring.java
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
package de.dfki.km.text20.util.rendering;

import java.awt.Color;

/**
 * @author rb
 *
 */
public class Coloring {
    /**
     * Returns a heatmap like color for the given value.
     * 
     * @param percentage
     * @return .
     */
    public static Color getHeatedColor(float percentage) {
        float hue = percentage * 0.4f;

        hue = (float) Math.pow(hue, 1.4);

        hue = (float) Math.min(hue, 0.4);
        hue = (float) Math.max(hue, 0.0);

        System.out.println(percentage + " -> " + hue);

        return Color.getHSBColor(hue, 1f, 1f);

    }
}
