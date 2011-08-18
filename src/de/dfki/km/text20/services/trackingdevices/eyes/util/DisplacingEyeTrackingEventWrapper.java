/*
 * DisplacingEyeTrackingEventWrapper.java
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
package de.dfki.km.text20.services.trackingdevices.eyes.util;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Point;
import java.util.List;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * A displacing eye tracking event wrapper.
 * 
 * TODO: Only center gaze is displaced right now, need to dispalce the other functions as well.
 * 
 * @since 1.4
 * @author Ralf Biedert
 */
public class DisplacingEyeTrackingEventWrapper extends EyeTrackingEventWrapper {
    /** */
    private static final long serialVersionUID = 2733860067954066679L;
    
    /** The displacement to apply. */
    private Point displacement;
    
    /**
     * Construct the object with the given displacement
     * 
     * @param trackingEvent
     * @param displacement 
     */
    public DisplacingEyeTrackingEventWrapper(EyeTrackingEvent trackingEvent, final Point displacement) {
        super(trackingEvent);
        
        this.displacement = displacement;
    }
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventWrapper#getGazeCenter()
     */
    @Override
    public Point getGazeCenter() {
        final Point center = this.originalEvent.getGazeCenter();
        center.x += this.displacement.x;
        center.y += this.displacement.y;
        return center;
    }
    
    
    /**
     * Displaces a number of events with the same displacement.
     * 
     * @param displacement
     * @param events
     * @return An array of all displaced events.
     */
    public static DisplacingEyeTrackingEventWrapper[] displace(Point displacement, EyeTrackingEvent...events) {
        if(events == null) return new DisplacingEyeTrackingEventWrapper[0];
        
        final DisplacingEyeTrackingEventWrapper[] rval = new DisplacingEyeTrackingEventWrapper[events.length];
        for (int i = 0; i < rval.length; i++) {
            rval[i]  = new DisplacingEyeTrackingEventWrapper(events[i], displacement);
        }
        
        return rval;
    }
    
    
    /**
     * Displaces a number of events with the same displacement.
     * 
     * @param displacement
     * @param events
     * @return An array of all displaced events.
     */
    public static List<EyeTrackingEvent> displace(Point displacement, List<EyeTrackingEvent> events) {
        if(events == null) return $.list();
        
        final List<EyeTrackingEvent> rval = $.list(events.size());
        for (int i = 0; i < events.size(); i++) {
            rval.add(new DisplacingEyeTrackingEventWrapper(events.get(i), displacement));
        }
        
        return rval;
    }
}
