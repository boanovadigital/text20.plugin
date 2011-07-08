/*
 * BrainTrackingEventContainer.java
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

import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

/**
 * Stores a brain tracking event
 *
 * @author Ralf Biedert
 */
public class BrainTrackingEventContainer extends AbstractSessionEvent implements BrainTrackingEvent {

    /** */
    private static final long serialVersionUID = -4224591581456166382L;

    @Element(required = false)
    public long hardwareEventTime = 0;
    
    /** */
    @Element
    public double[] readings = new double[0];

    /**
     * @param trackingEvent
     */
    public BrainTrackingEventContainer(final BrainTrackingEvent trackingEvent) {
        this.hardwareEventTime = trackingEvent.getEventTime();
        this.readings = trackingEvent.getReadings();
    }

    /** */
    protected BrainTrackingEventContainer() { }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.braintrackingdevices.BrainTrackingEvent#getEventTime()
     */
    @Override
    public long getEventTime() {
        return this.hardwareEventTime == 0 ? this.originalEventTime : this.hardwareEventTime;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent#getReadings()
     */
    @Override
    public double[] getReadings() {
        return this.readings;
    }
}
