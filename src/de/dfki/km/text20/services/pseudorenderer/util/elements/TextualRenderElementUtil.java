/*
 * TextualRenderElementUtil.java
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
package de.dfki.km.text20.services.pseudorenderer.util.elements;

import java.awt.Point;
import java.awt.Rectangle;

import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * Util functions for a textual render element.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class TextualRenderElementUtil extends RenderElementUtil implements
        TextualRenderElement {

    static {
        
    }
    
    /**
     * @param renderElement
     */
    public TextualRenderElementUtil(TextualRenderElement renderElement) {
        super(renderElement);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getContent()
     */
    @Override
    public String getContent() {
        return cast(TextualRenderElement.class).getContent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setContent(java.lang.String)
     */
    @Override
    public void setContent(String content) {
        cast(TextualRenderElement.class).setContent(content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getWordID()
     */
    @Override
    public int getWordID() {
        return cast(TextualRenderElement.class).getWordID();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setWordID(int)
     */
    @Override
    public void setWordID(int id) {
        cast(TextualRenderElement.class).setWordID(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setTextID(int)
     */
    @Override
    public void setTextID(int id) {
        cast(TextualRenderElement.class).setTextID(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getTextID()
     */
    @Override
    public int getTextID() {
        return cast(TextualRenderElement.class).getTextID();
    }

    /**
     * For a point inside the render element's rectangle, return the character position.
     * 
     * @since 1.4
     * @param point
     * @return The character position.
     */
    public int characterPosition(Point point) {
        final String content = getContent();
        final Rectangle geometry = getGeometry(CoordinatesType.DOCUMENT_BASED);
        
        
        double perc = ((double) point.x) / geometry.width;
        int rval = (int) (perc * content.length());
        return rval;
    }
}
