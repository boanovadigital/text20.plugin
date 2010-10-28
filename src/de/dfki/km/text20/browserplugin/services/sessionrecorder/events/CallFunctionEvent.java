/*
 * CallFunctionEvent.java
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
 * @author rb
 *
 */
public class CallFunctionEvent extends AbstractSessionEvent {

    /** */
    private static final long serialVersionUID = 6946324228563983022L;

    /** */
    @Element
    public String function;

    /** */
    public CallFunctionEvent() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param function
     */
    public CallFunctionEvent(final String function) {
        this.function = function;
    }

}
