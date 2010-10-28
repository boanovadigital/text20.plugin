/*
 * PseudorendererManager.java
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

import net.xeoh.plugins.base.Plugin;

/**
 * Manages pseudo-renderers. A pseudo-renderer is a (hidden) datastructure which keeps information liked to 
 * a "true" rendering device, like it's windowGeometry and contained shapes.
 *  
 * @author rb
 *
 */
public interface PseudorendererManager extends Plugin {

    /**
     * Create a new renderer.
     * 
     * @return .
     */
    public Pseudorenderer createPseudorenderer();
}
