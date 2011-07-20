/*
 * TextualRenderElementDummy.java
 * 
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package de.dfki.km.text20.services.pseudorenderer.util.elements;

import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * Simple textual render element dummy.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class TextualRenderElementDummy extends RenderElementDummy implements TextualRenderElement {

    /** */
    private static final long serialVersionUID = 3769335732135173969L;

    /** */
    public String content = null;
    
    /** */
    public int wordID = 0;

    /** */
    public int textID = 0;

    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getContent()
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setContent(java.lang.String)
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getWordID()
     */
    @Override
    public int getWordID() {
        return this.wordID;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setWordID(int)
     */
    @Override
    public void setWordID(int id) {
        this.wordID = id;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#setTextID(int)
     */
    @Override
    public void setTextID(int id) {
        this.textID = id;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement#getTextID()
     */
    @Override
    public int getTextID() {
        return this.textID;
    }
    
    @Override
    public String toString() {
        return getContent();
    }
}
