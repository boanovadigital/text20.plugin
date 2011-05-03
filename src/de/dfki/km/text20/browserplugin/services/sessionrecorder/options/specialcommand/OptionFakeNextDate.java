/*
 * OptionFakeNextDate.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.specialcommand;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.SpecialCommandOption;

/**
 * Tells the session recorder to fake the next event date.
 * 
 * @author Ralf Biedert
 */
public class OptionFakeNextDate implements SpecialCommandOption {

    /** */
    private static final long serialVersionUID = 7045264790144268252L;

    /** */
    private final long startdate;

    /**
     * Constructs a new fake date. 
     * 
     * @param next The time to use for the next event instead of the current time.
     * @see System 
     */
    public OptionFakeNextDate(long next) {
        this.startdate = next;
    }

    /**
     * Returns the date. 
     * 
     * @return The date.
     */
    public long getDate() {
        return this.startdate;
    }

}
