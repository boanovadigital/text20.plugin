/*
 * ElementMetaInformation.java
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

import org.simpleframework.xml.Element;

/**
 * @author Ralf Biedert
 *
 */
public class ElementMetaInformation extends AbstractSessionEvent {

    /** */
    private static final long serialVersionUID = -251748408546738653L;

    /** */
    @Element(required = false)
    public String key;

    /** */
    @Element(required = false)
    public String id;

    /** */
    @Element(required = false)
    public String value;

    /**
     * @param id
     * @param key
     * @param value
     */
    public ElementMetaInformation(final String id, String key, String value) {
        this.id = id;
        this.value = value;
        this.key = key;
    }

    protected ElementMetaInformation() {
        // 
    }
}
