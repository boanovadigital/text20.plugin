/*
 * GridEntry.java
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

/**
 * Manages one grid field.
 * 
 * @author Ralf Biedert
 */
public class GridEntry {
    /**
     * The status of the entry.
     * 
     * @author Ralf Biedert
     */
    public enum GridStatus {
        /** We obtianed a proper displaement in the region */
        SET,
        /** Unknown means no displacement known */
        UNKNOWN
    }

    long dateOfSet = 0;

    GridStatus gridStatus = GridStatus.UNKNOWN;

    int xdisplacement = 0;
    int ydisplacement = 0;
}
