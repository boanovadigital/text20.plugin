/*
 * ElementGeometryEvent.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.events;

import java.awt.Rectangle;

import org.simpleframework.xml.Element;

/**
 * 
 * @author Ralf Biedert
 *
 */
public class ElementGeometryEvent extends AbstractSessionEvent {

    /** */
    private static final long serialVersionUID = -2517484085749543653L;

    /** */
    @Element(required = false)
    public String content;

    /** */
    @Element(required = false)
    public Rectangle documentRectangle;

    /** */
    @Element(required = false)
    public String id;

    /** */
    @Element(required = false)
    public String type;

    /**
     *
     * @param id
     * @param type
     * @param content
     * @param r
     */
    public ElementGeometryEvent(final String id, final String type, final String content,
                                final Rectangle r) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.documentRectangle = r;
    }

    protected ElementGeometryEvent() {
        // 
    }
}
