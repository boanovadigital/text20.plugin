/*
 * TrackingEventContainer.java
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

import java.awt.Point;

import org.simpleframework.xml.Element;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 *
 *
 * @author rb
 */
public class EyeTrackingEventContainer extends AbstractSessionEvent implements
        EyeTrackingEvent {

    /** */
    private static final long serialVersionUID = -4224591581456166382L;

    /** */
    @Element
    private Point combinedCenter;

    @Element(required = false)
    private float headPosition[] = new float[3];

    @Element(required = false)
    private float leftEyeDistance = 0;

    @Element(required = false)
    private float[] leftEyePosition = new float[3];

    @Element(required = false)
    private float pupilSizeLeft = 0;

    @Element(required = false)
    private float pupilSizeRight = 0;

    @Element(required = false)
    private float rightEyeDistance = 0;

    @Element(required = false)
    private float[] rightEyePosition = new float[3];

    @Element(required = false)
    private boolean validity = true;

    // If version = 0 this is an old event
    @SuppressWarnings("unused")
    @Element(required = false)
    private int version = 0;

    /**
     * @param trackingEvent
     */
    public EyeTrackingEventContainer(final EyeTrackingEvent trackingEvent) {
        // We should not use the original's event time, as it is set on the remote host and the times may differ.
        this.combinedCenter = trackingEvent.getGazeCenter();
        this.headPosition = trackingEvent.getHeadPosition();
        this.pupilSizeLeft = trackingEvent.getPupilSizeLeft();
        this.pupilSizeRight = trackingEvent.getPupilSizeRight();

        this.leftEyeDistance = trackingEvent.getLeftEyeDistance();
        this.rightEyeDistance = trackingEvent.getRightEyeDistance();
        this.leftEyePosition = trackingEvent.getLeftEyePosition();
        this.rightEyePosition = trackingEvent.getRightEyePosition();

        // Current version
        this.version = 1;
    }

    protected EyeTrackingEventContainer() {
        //
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#areValid(de.dfki.km.augmentedtext.services.trackingdevices.TrackingEventValidity[])
     */
    @Override
    public boolean areValid(final EyeTrackingEventValidity... validities) {
        boolean rval = true;
        for (final EyeTrackingEventValidity v : validities) {
            if (v == EyeTrackingEventValidity.CENTER_POSITION_VALID) {
                rval &= this.validity;

                // Override validity for special points ...
                if (this.combinedCenter.x <= 0 && this.combinedCenter.y <= 0) {
                    rval = false;
                }
            }
        }
        return rval;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#eventTime()
     */
    @Override
    public long getEventTime() {
        return this.originalEventTime;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getGazeCenter()
     */
    @Override
    public Point getGazeCenter() {
        return (Point) this.combinedCenter.clone();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getHeadPosition()
     */
    @Override
    public float[] getHeadPosition() {
        return this.headPosition;
    }

    @Override
    public float getLeftEyeDistance() {
        return this.leftEyeDistance;
    }

    @Override
    public float[] getLeftEyePosition() {
        return this.leftEyePosition;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#pupilSizeLeft()
     */
    @Override
    public float getPupilSizeLeft() {
        return this.pupilSizeLeft;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#pupilSizeRight()
     */
    @Override
    public float getPupilSizeRight() {
        return this.pupilSizeRight;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getRightEyeDistance()
     */
    @Override
    public float getRightEyeDistance() {
        return this.rightEyeDistance;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.trackingdevices.TrackingEvent#getRightEyePosition()
     */
    @Override
    public float[] getRightEyePosition() {
        return this.rightEyePosition;
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
}
