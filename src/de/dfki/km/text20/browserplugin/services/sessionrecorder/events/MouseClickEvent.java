/*
 * MouseClickEvent.java
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
public class MouseClickEvent extends AbstractSessionEvent {

    /** */
    public static final int DOUBLE = 2;

    /** */
    public static final int SINGLE = 1;

    /** */
    private static final long serialVersionUID = 286254463530352672L;

    /** */
    @Element
    public int button;

    /** */
    @Element
    public int type;

    /** */
    @Element(required = false)
    public int x;

    /** */
    @Element(required = false)
    public int y;

    /** */
    public MouseClickEvent() {
        //
    }

    /**
     * @param x
     * @param y
     * @param type
     * @param button
     */
    public MouseClickEvent(final int x, final int y, final int type, final int button) {
        this.type = type;
        this.button = button;
        this.x = x;
        this.y = y;
    }
}
