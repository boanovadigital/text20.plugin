/*
 * FixationDummy.java
 * 
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util;

import static net.jcores.shared.CoreKeeper.$;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventDummy;

/**
 * Allows you to quickly create a {@link Fixation} object where one is needed. The class will also
 * emulate contained {@link EyeTrackingEvent} objects.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class FixationDummy implements Fixation, Cloneable, Serializable {
    /** */
    private static final long serialVersionUID = 1155113877622191307L;

    /** The assumed standard eye tracking rate for simulated events */
    public static final double DEFAULT_TRACKING_RATE = 60.0;

    /** Our center */
    public Point center = new Point();

    /** The list of eye tracking events we contain */
    public List<EyeTrackingEvent> events = new ArrayList<EyeTrackingEvent>();

    /**
     * Creates a {@link Fixation} dummy.
     * 
     */
    public FixationDummy() {}

    /**
     * Simulates a fixation for the given point with the given duration. Also ensures there are
     * proper {@link EyeTrackingEvent} objects.
     * 
     * @param c The center to fake for.
     * @param duration The duration of the fixation.
     * @return This object again.
     */
    public FixationDummy simulate(Point c, long duration) {
        this.center = c;
        this.events.clear();

        final long start = System.currentTimeMillis() - duration / 2;
        
        // Add the tracking events
        final double numEvents = duration / DEFAULT_TRACKING_RATE;
        for (int i = 0; i < numEvents; i++) {
            this.events.add(new EyeTrackingEventDummy().simulate(c).time((start + i * (long) DEFAULT_TRACKING_RATE)));
        }
        
        // And always add a last event so what we match the requested duarion. 
        this.events.add(new EyeTrackingEventDummy().simulate(c).time((start + duration)));

        // Return this element again.
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation#getCenter()
     */
    @Override
    public Point getCenter() {
        return $.clone(this.center);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation#getTrackingEvents()
     */
    @Override
    public List<EyeTrackingEvent> getTrackingEvents() {
        return new ArrayList<EyeTrackingEvent>(this.events);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Fixation (" + this.center.x + "/" + this.center.y + ")";
    }
}
