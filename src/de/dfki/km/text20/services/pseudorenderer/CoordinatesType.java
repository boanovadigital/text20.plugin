/*
 * CoordinatesType.java
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
package de.dfki.km.text20.services.pseudorenderer;

/**
 * Specifies relative to which coordinate system points are being based.
 * 
 * @author Ralf Biedert
 * @since 1.0
 * @see Pseudorenderer
 */
public enum CoordinatesType {

    /** Points are document based */
    DOCUMENT_BASED,
    
    /** Points are screen based */
    SCREEN_BASED,

    /** Coordinates are based on the currently visible rectangle, i.e., (0,0) == (scrollPosX, scrollPosY) */
    VIEWPORT_BASED
}
