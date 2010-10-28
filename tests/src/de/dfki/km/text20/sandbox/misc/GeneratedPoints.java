/*
 * GeneratedPoints.java
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml.SessionRecordImpl;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 * @author rb
 *
 */
public class GeneratedPoints {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        final long time = System.currentTimeMillis();

        final SessionRecordImpl sr = new SessionRecordImpl(new Dimension(500, 500));

        sr.overrideDateForNextEvent(new Date(time));
        sr.updateGeometry(new Rectangle(100, 100, 100, 100));
        final Random rnd = new Random();
        int i = 0;
        while (i < 100) {
            final int j = i;
            sr.overrideDateForNextEvent(new Date(time + 30 * j));
            sr.trackingEvent(new EyeTrackingEvent() {
                @Override
                public boolean areValid(final EyeTrackingEventValidity... validities) {
                    // TODO Auto-generated method stub
                    return true;
                }

                @Override
                public long getEventTime() {
                    System.out.println(j);
                    return time + 30L * j;
                }

                @Override
                public Point getGazeCenter() {
                    return new Point(150 + rnd.nextInt(50) - 25, 150 + rnd.nextInt(50) - 25);
                }

                @Override
                public float[] getHeadPosition() {
                    return new float[3];
                }

                @Override
                public float getLeftEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float[] getLeftEyePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public float getPupilSizeLeft() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float getPupilSizeRight() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float getRightEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public float[] getRightEyePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Point getLeftEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public float[] getLeftEyeGazePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public Point getRightEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public float[] getRightEyeGazePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }
            });

            i++;
        }
        sr.writeTo("D:\\dateien\\session.xml");
    }

}
