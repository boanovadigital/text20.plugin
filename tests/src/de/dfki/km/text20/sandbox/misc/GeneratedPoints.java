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
                public boolean areValid(final EyeTrackingEventValidity... validities) {
                    // TODO Auto-generated method stub
                    return true;
                }

                public long getEventTime() {
                    System.out.println(j);
                    return time + 30L * j;
                }

                public Point getGazeCenter() {

                    return new Point(150 + rnd.nextInt(50) - 25, 150 + rnd.nextInt(50) - 25);
                }

                public float[] getHeadPosition() {
                    return new float[3];
                }

                public float getLeftEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public float[] getLeftEyePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public float getPupilSizeLeft() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public float getPupilSizeRight() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public float getRightEyeDistance() {
                    // TODO Auto-generated method stub
                    return 0;
                }

                public float[] getRightEyePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public Point getLeftEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public float[] getLeftEyeGazePosition() {
                    // TODO Auto-generated method stub
                    return null;
                }

                public Point getRightEyeGazePoint() {
                    // TODO Auto-generated method stub
                    return null;
                }

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
