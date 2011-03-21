/*
 * TestFixationUtils.java
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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.util.FixationsUtil;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author rb
 *
 */
public class TestFixationUtils {
    /**
     * @param args
     * @throws URISyntaxException
     * @throws IOException 
     */
    public static void main(final String[] args) throws URISyntaxException, IOException {
        final Fixation f[] = new Fixation[] { f(100, 100), f(200, 200), null, f(300, 300) };
        final List<Fixation> fl = Arrays.asList(f);

        final FixationsUtil fu = new FixationsUtil(fl);

        System.out.println(fu.getAllAngles()[0]);
        System.out.println(fu.getAllAngles()[1]);        
        System.out.println(fu.getAllAngles()[2]);
        
        System.out.println(fu.getAvgVerticalDeviation());

    }

    /**
     * @param x
     * @param y
     * @return .
     */
    public static Fixation f(final int x, final int y) {
        return new Fixation() {

            @Override
            public List<EyeTrackingEvent> getTrackingEvents() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Point getCenter() {
                return new Point(x, y);
            }
        };
    }
}
