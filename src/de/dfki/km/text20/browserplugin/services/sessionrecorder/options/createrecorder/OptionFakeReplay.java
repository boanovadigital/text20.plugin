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

import java.awt.Dimension;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.CreateRecorderOption;

/**
 * If passed, the {@link SessionRecorder} will generate a fake repaly with the given 
 * start date that will be saved to the given directory. 
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class OptionFakeReplay implements CreateRecorderOption {

    /** */
    private static final long serialVersionUID = 7045264790144268252L;

    /** */
    private final long startdate;

    /** */
    private final String file;

    /** */
    private final Dimension screenSize;

    /**
     * Constructs a new fake replay. 
     * 
     * @param file The file to save the replay to.
     * @param screenSize The assumed screen size.
     * @param startdate The time when the fake replay should start.
     * 
     */
    public OptionFakeReplay(String file, Dimension screenSize, long startdate) {
        this.file = file;
        this.screenSize = screenSize;
        this.startdate = startdate;
    }

    /**
     * Returns the start date.
     * 
     * @return The start date.
     */
    public long getStartDate() {
        return this.startdate;
    }

    /**
     * Returns the file of the replay.
     * 
     * @return The file.
     */
    public String getFile() {
        return this.file;
    }

    /**
     * Returns the screen size.
     * 
     * @return The screen size.
     */
    public Dimension getScreenSize() {
        return this.screenSize;
    }
}
