/*
 * RenderElementDummy.java
 * 
 * Copyright (c) 2011, Ralf Biedert All rights reserved.
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

import java.awt.Rectangle;
import java.io.Serializable;

import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;

/**
 * A simple render element dummy. 
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class RenderElementDummy implements RenderElement, Cloneable, Serializable {
    /** */
    private static final long serialVersionUID = 1440806884151973182L;

    /** */
    public CoordinatesType coordinatesType = null;
    
    /** */
    public Rectangle geometry = null;
    
    /** */
    public String id = null;
    
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getCoordinatesType()
     */
    @Override
    public CoordinatesType getCoordinatesType() {
        return this.coordinatesType;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getGeometry(de.dfki.km.text20.services.pseudorenderer.CoordinatesType)
     */
    @Override
    public Rectangle getGeometry(CoordinatesType type) {
        return this.geometry;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return this.id;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute)
     */
    @Override
    public Serializable getMetaAttribute(RenderElementMetaAttribute key) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#getZIndex()
     */
    @Override
    public int getZIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#hasMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute)
     */
    @Override
    public boolean hasMetaAttribute(RenderElementMetaAttribute key) {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#isVisible()
     */
    @Override
    public boolean isVisible() {
        return true;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setGeometry(java.awt.Rectangle, de.dfki.km.text20.services.pseudorenderer.CoordinatesType)
     */
    @Override
    public void setGeometry(Rectangle rectangle, CoordinatesType type) {
        this.geometry = rectangle;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setMetaAttribute(de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute, java.io.Serializable)
     */
    @Override
    public void setMetaAttribute(RenderElementMetaAttribute key, Serializable value) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.pseudorenderer.RenderElement#setZIndex(int)
     */
    @Override
    public void setZIndex(int zindex) {
        // TODO Auto-generated method stub
    }

}
