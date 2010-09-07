/*
 * PersualLevel.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.perusal;

/**
 * The level of perusal currently observed.
 * 
 * @author rb
 *
 */
@Deprecated
public enum PerusalLevel {
    /**
     * If the progress is significantly faster than reading, the linewise pattern appears to dissolve 
     * but the progress is still happening on text we classify it as fest skimming  
     */
    FAST_SKIMMING,

    /**
     * If there is reading progress significantly slower than reading we classify it as
     * slow reading.
     */
    INTENSIVE_READING,

    /**
     * This is the default mode used upon calibration.
     */
    READING,

    /**
     * If the reading progress is significantly faster than reading but still mostly line base, we classify
     * it as skimming 
     */
    SKIMMING
}
