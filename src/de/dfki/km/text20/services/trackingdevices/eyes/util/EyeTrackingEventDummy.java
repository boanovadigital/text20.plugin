 /*
 * TrackingEventDummy.java
 *
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
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
package de.dfki.km.text20.services.trackingdevices.eyes.util;

import static net.jcores.CoreKeeper.$;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 * Wraps tracking events and allows changing of its values.
 * 
 * @author Ralf Biedert
 */
public class EyeTrackingEventDummy implements EyeTrackingEvent {

    /** Default dimension to use */
    static Dimension defaultDimension;

    /** Get default dimensions */
    static {
        try {
            defaultDimension = Toolkit.getDefaultToolkit().getScreenSize();
        } catch (Throwable t) {
            defaultDimension = new Dimension(1024, 768);
        }
    }

    /** Size of the screen */
    public Dimension screenSize;

    /** Creation time */
    public long eventTime;

    /** Where we looked at */
    public Point gazeCenter;

    /** Where we looked at (left eye) */
    public Point leftGazePoint;

    /** Where we looked at (right eye) */
    public Point rightGazePoint;

    /** Left gaze point */
    public float[] leftGazePos;

    /** Right gaze point */
    public float[] rightGazePos;

    /** Head position */
    public float[] headPosition;

    /** Eye distances */
    public float[] eyeDistances;

    /** Left eye positon */
    public float[] leftEyePosition;

    /** Right eye positon */
    public float[] rightEyePosition;

    /** Pupil sizes */
    public float[] pupilSizes;

    /**
     * Creates an empty dummy with the current screen size
     */
    public EyeTrackingEventDummy() {
        this(null);
    }
    
    /**
     * Creates an empty dummy with a given screen size.
     * 
     * @param screenSize Size of the screen.
     */
    public EyeTrackingEventDummy(Dimension screenSize) {
        this.screenSize = $(screenSize).get(defaultDimension);
        this.eventTime = System.currentTimeMillis();
        this.gazeCenter = new Point(0, 0);
        this.leftGazePoint = new Point(0, 0);
        this.rightGazePoint = new Point(0, 0);
        this.leftGazePos = new float[2];
        this.rightGazePos = new float[2];
        this.headPosition = new float[3];
        this.eyeDistances = new float[] { 0, 0 };
        this.leftEyePosition = new float[3];
        this.rightEyePosition = new float[3];
        this.pupilSizes = new float[] { 0, 0 };
    }

    /**
     * Creates a gaze point with some sensible values, simulating certain parameters.
     * 
     * @param screen Point on the screen where the user looked at.
     * @return This object
     */
    public EyeTrackingEventDummy simulate(Point screen) {
        this.eventTime = System.currentTimeMillis();

        this.gazeCenter = (Point) screen.clone();
        this.leftGazePoint = (Point) screen.clone();
        this.rightGazePoint = (Point) screen.clone();

        this.leftGazePos[0] = (float) (screen.x / this.screenSize.getWidth());
        this.leftGazePos[1] = (float) (screen.y / this.screenSize.getHeight());

        this.rightGazePos[0] = (float) (screen.x / this.screenSize.getWidth());
        this.rightGazePos[1] = (float) (screen.y / this.screenSize.getHeight());

        this.eyeDistances[0] = 300.0f;
        this.eyeDistances[1] = 300.0f;

        this.leftEyePosition[0] = 0.4f;
        this.leftEyePosition[1] = 0.5f;
        this.leftEyePosition[2] = 300f;

        this.rightEyePosition[0] = 0.6f;
        this.rightEyePosition[1] = 0.5f;
        this.rightEyePosition[2] = 300f;

        this.pupilSizes[0] = 3.5f;
        this.pupilSizes[1] = 3.5f;

        return this;
    }

    /**
     * Sets the current time.
     * 
     * @param time The time to set.
     * @return This object.
     */
    public EyeTrackingEventDummy time(long time) {
        this.eventTime = time;
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#areValid(de.dfki.km.text20.services.trackingdevices
     * .eyes.EyeTrackingEventValidity[])
     */
    @Override
    public boolean areValid(final EyeTrackingEventValidity... validities) {
        return true; // TODO
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.common.TrackingEvent#getEventTime()
     */
    @Override
    public long getEventTime() {
        return this.eventTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getGazeCenter()
     */
    @Override
    public Point getGazeCenter() {
        return (Point) this.gazeCenter.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getHeadPosition()
     */
    @Override
    public float[] getHeadPosition() {
        return this.headPosition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getLeftEyeDistance()
     */
    @Override
    public float getLeftEyeDistance() {
        return this.eyeDistances[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getLeftEyePosition()
     */
    @Override
    public float[] getLeftEyePosition() {
        return this.leftEyePosition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getPupilSizeLeft()
     */
    @Override
    public float getPupilSizeLeft() {
        return this.pupilSizes[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getPupilSizeRight()
     */
    @Override
    public float getPupilSizeRight() {
        return this.pupilSizes[1];
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getRightEyeDistance()
     */
    @Override
    public float getRightEyeDistance() {
        return this.eyeDistances[1];

    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getRightEyePosition()
     */
    @Override
    public float[] getRightEyePosition() {
        return this.rightEyePosition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getLeftEyeGazePoint()
     */
    @Override
    public Point getLeftEyeGazePoint() {
        return this.leftGazePoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getLeftEyeGazePosition()
     */
    @Override
    public float[] getLeftEyeGazePosition() {
        return this.leftGazePos;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getRightEyeGazePoint()
     */
    @Override
    public Point getRightEyeGazePoint() {
        return this.rightGazePoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent#getRightEyeGazePosition()
     */
    @Override
    public float[] getRightEyeGazePosition() {
        return this.rightGazePos;
    }
    
    @Override
    public String toString() {
        return "EyeTrackingEvent {time:" + this.eventTime + ", pos:" + this.gazeCenter.x + "/" + this.gazeCenter.y + ")";
    }
}
