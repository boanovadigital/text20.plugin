/*
 * AbstractSessionEvent.java
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

import java.io.Serializable;

import org.simpleframework.xml.Element;

/**
 *
 * @author Ralf Biedert
 */
public abstract class AbstractSessionEvent implements Serializable, SessionEvent {
    /** */
    private static final long serialVersionUID = -5555418030293958091L;

    /** */
    @Element
    public long originalEventTime;

    /**
     *
     */
    public AbstractSessionEvent() {
        this(System.currentTimeMillis());
    }

    /**
     *
     * @param date
     */
    public AbstractSessionEvent(final long date) {
        this.originalEventTime = date;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.util.recorder.events.Event#getTime()
     */

    @Override
    public long getTime() {
        return this.originalEventTime;
    }
}
