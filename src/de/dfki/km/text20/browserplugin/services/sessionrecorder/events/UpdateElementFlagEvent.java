/*
 * UpdateElementFlagEvent.java
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
 * 
 * @author rb
 */
public class UpdateElementFlagEvent extends AbstractSessionEvent {

    /** */
    private static final long serialVersionUID = -1597902812313924123L;

    /** */
    @Element(required = false)
    public String flag;

    /** */
    @Element(required = false)
    public String id;

    /** */
    @Element(required = false)
    public boolean value;

    /** */
    public UpdateElementFlagEvent() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param id
     * @param flag
     * @param value
     */
    public UpdateElementFlagEvent(final String id, final String flag, final boolean value) {
        this.id = id;
        this.flag = flag;
        this.value = value;
    }
}
