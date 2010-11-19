/*
 * GraphicalRenderElementImpl.java
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
package de.dfki.km.text20.services.pseudorenderer.impl;

import de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement;

/**
 * @author rb
 *
 */
public class GraphicalRenderElementImpl extends RenderElementImpl implements
        GraphicalRenderElement {

    /** Image source of this element */
    private String source;
    
    /**
     * @param pseudorendererImpl
     */
    GraphicalRenderElementImpl(PseudorendererImpl pseudorendererImpl) {
        super(pseudorendererImpl);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement#getSrc()
     */
    @Override
    public String getSource() {
        return this.source;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement#setSrc(java.lang.String)
     */
    @Override
    public void setSource(String src) {
        this.source = src;
        updateChangeID();
    }

}
