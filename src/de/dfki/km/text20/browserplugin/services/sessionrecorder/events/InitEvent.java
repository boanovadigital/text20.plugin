/*
 * InitEvent.java
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
public class InitEvent extends AbstractSessionEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 5381862907326035060L;

    /** */
    @Element(required = false)
    public int version;

    /**
     * Construct a default init event
     */
    public InitEvent() {
        //
    }

    /**
     * Construct an init event with a given version
     * 
     * @param version 
     */
    public InitEvent(int version) {
        this.version = version;
    }
}
