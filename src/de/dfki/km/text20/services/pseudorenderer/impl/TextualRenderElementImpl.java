/*
 * TextualRenderElementImpl.java
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

import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * @author Ralf Biedert
 *
 */
public class TextualRenderElementImpl extends RenderElementImpl implements
        TextualRenderElement {

    private String content = null;

    private int wordID = -1;

    private int textID = -1;

    /**
     * @param pseudorendererImpl
     */
    TextualRenderElementImpl(PseudorendererImpl pseudorendererImpl) {
        super(pseudorendererImpl);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#getContent()
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.RenderElement#setContent(java.lang.String)
     */
    @Override
    public void setContent(final String content) {
        this.content = content;
        updateChangeID();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.renderelements.TextualRenderElement#getTextID()
     */
    @Override
    public int getTextID() {
        return this.textID;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.renderelements.TextualRenderElement#getWordID()
     */
    @Override
    public int getWordID() {
        return this.wordID;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.renderelements.TextualRenderElement#setTextID(int)
     */
    @Override
    public void setTextID(int id) {
        this.textID = id;
        updateChangeID();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.pseudorenderer.renderelements.TextualRenderElement#setWordID(int)
     */
    @Override
    public void setWordID(int id) {
        this.wordID = id;
        updateChangeID();
    }
}
