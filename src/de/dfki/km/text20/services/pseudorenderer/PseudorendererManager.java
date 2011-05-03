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
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.PseudorendererItem;

/**
 * Manages and provides pseudo renderers. When in extension mode the {@link Pseudorenderer} can be obtained through 
 * the {@link PseudorendererItem} (see the {@link BrowserAPI}).  
 *  
 * @author Ralf Biedert
 * @since 1.0
 */
public interface PseudorendererManager extends Plugin {

    /**
     * Create a new renderer.
     * 
     * @return A new, empty renderer.
     */
    public Pseudorenderer createPseudorenderer();
}
