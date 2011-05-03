/*
 * TextualRenderElement.java
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
package de.dfki.km.text20.services.pseudorenderer.renderelements;

import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.util.TextualRenderElementUtil;

/**
 * Represents a textual render element (e.g., word on the page).
 * 
 * @author Ralf Biedert
 * @since 1.0
 * @see TextualRenderElementUtil
 */
public interface TextualRenderElement extends RenderElement {
    /**
     * Return the content of this element.
     * 
     * @return The content.
     */
    public String getContent();

    /**
     * Sets the content of an element.
     * 
     * @param content The text this element represents.
     */
    public void setContent(String content);

    /**
     * Returns the word ID of this element. Word IDs are ordered and usually 
     * start with 0. If you order all word ID of the same text ID you should be able
     * to reconstruct the text.
     * 
     * @return -1 means invalid / unknown.
     */
    public int getWordID();

    /**
     * Sets the word ID of this element. Note that a higher ID implicates the word comes
     * after a word with a lower ID.
     * 
     * @param id -1 means invalid / unknown.
     */
    public void setWordID(int id);

    /**
     * Sets the text ID of this element. Words with the same text ID are supposed to be
     * ordered by their word ID. Text IDs might be random. 
     * 
     * @param id -1 means invalid / unknown.
     */
    public void setTextID(int id);

    /**
     * Returns the text ID.
     * 
     * @return -1 means invalid / unknown.
     */
    public int getTextID();
}
