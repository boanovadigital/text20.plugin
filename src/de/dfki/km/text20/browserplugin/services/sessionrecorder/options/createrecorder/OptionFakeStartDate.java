/*
 * OptionFakeStartDate.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.createrecorder;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;

/**
 * @author rb
 *
 */
public class OptionFakeStartDate implements CreateRecorderOption {

    /** */
    private static final long serialVersionUID = 7045264790144268252L;

    /** */
    private final long startdate;

    /**
     * @param startdate
     *
     */
    public OptionFakeStartDate(long startdate) {
        this.startdate = startdate;
    }

    /**
     * @return the startdate
     */
    public long getStartDate() {
        return this.startdate;
    }

}
