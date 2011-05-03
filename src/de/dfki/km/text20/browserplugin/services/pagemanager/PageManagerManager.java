/*
 * PageManagerManager.java
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
package de.dfki.km.text20.browserplugin.services.pagemanager;

import net.xeoh.plugins.base.Plugin;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;

/**
 * Spawns {@link PageManager}s for {@link Pseudorenderer}s. You don't need to 
 * care about this plugin, neither in extension mode, nor in library mode. 
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public interface PageManagerManager extends Plugin {
    /**
     * Creates a new {@link PageManager} for a given {@link Pseudorenderer}.
     * 
     * @param pseudorenderer The {@link Pseudorenderer} for which we create a {@link PageManager}.
     * @return A new {@link PageManager}
     */
    public PageManager createPageManager(Pseudorenderer pseudorenderer);
}
