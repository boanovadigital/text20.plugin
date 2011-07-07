/*
 * TestSerialization.java
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
package de.dfki.km.text20.sandbox.misc;

import java.awt.Point;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationDummy;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Ralf Biedert
 *
 */
public class TestDummy {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        FixationDummy dummy = new FixationDummy().simulate(new Point(100, 100), 100);
        FixationUtil util = new FixationUtil(dummy);
        
        System.out.println(dummy);
        System.out.println(util.getFixationDuration());
        List<EyeTrackingEvent> trackingEvents = dummy.getTrackingEvents();
        for (EyeTrackingEvent eyeTrackingEvent : trackingEvents) {
            System.out.println(eyeTrackingEvent);
        }
    }
}
