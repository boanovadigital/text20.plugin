/*
 * RecalibrationWidget.java
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
package de.dfki.km.text20.browserplugin.services.onscreenmenu.impl.widgets;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.util.filter.AbstractFilter;
import de.dfki.km.text20.util.filter.centralpoint.SmoothingFilter;

/**
 * 
 * @author rb
 *
 */
public class RecalibrationWidget extends JPanel {

    /** */
    private static final int DEFAULT_SIZE = 100;

    /** */
    private static final long serialVersionUID = 7735211336461768013L;

    /** */
    boolean calibrating = false;

    /** */
    Point calibrationCenter = new Point();

    /** */
    Point calibrationOffset = null;

    /** */
    float calibrationSize = DEFAULT_SIZE;

    /** */
    AbstractFilter filter = new SmoothingFilter(10);

    /** Where did the last filtered gaze point hit? */
    Point lastGazePoint = new Point();

    /** */
    public RecalibrationWidget() {
        initGUI();

    }

    /**
     * Override this methods to obtain the results.
     */
    public void finished() {
        //
    }

    /**
     * @return the calibrationOffset
     */
    public Point getCalibrationOffset() {
        return this.calibrationOffset;
    }

    /**
     * 
     * @param _event
     */
    public void newGazePoint(final EyeTrackingEvent _event) {
        // Only do something if we are started
        if (!this.calibrating) return;

        // FIXME: Check if events are sane better way!!!!!
        if (_event.getGazeCenter() == null) return;
        if (_event.getGazeCenter().x == 0) return;

        // Obtain center of gaze
        final EyeTrackingEvent event = this.filter.filterEvent(_event);

        final Point p = (Point) event.getGazeCenter().clone();

        // Convert to widget coordinats
        SwingUtilities.convertPointFromScreen(p, this);

        // Memorize points
        this.lastGazePoint = p;

        // Check if gaze is really inside
        if (new Rectangle(new Point(), getSize()).contains(p)) {

            this.calibrationSize *= 0.95;
        } else {
            this.calibrationSize = DEFAULT_SIZE;
        }

        // Check if calibration finished
        if (this.calibrationSize < 10) {
            calibrationFinished();
        }

        repaint();
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);

        g.setColor(new Color(1f, 0f, 0f, 0.7f));
        g.fillOval((int) (this.calibrationCenter.x - this.calibrationSize / 2), (int) (this.calibrationCenter.y - this.calibrationSize / 2), (int) this.calibrationSize, (int) this.calibrationSize);
    }

    /**
     * Start a new calibration
     */
    public void start() {

        // Wait a bit with the calibration
        final Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    //
                }
                RecalibrationWidget.this.calibrating = true;
            }

        });
        t.start();

        this.calibrationCenter.x = getWidth() / 2;
        this.calibrationCenter.y = getHeight() / 2;
        this.calibrationSize = DEFAULT_SIZE;
        repaint();
    }

    /**
     * Called when the calibration finishes
     */
    private void calibrationFinished() {
        // TODO Auto-generated method stub

        this.calibrating = false;
        this.calibrationOffset = new Point(-(this.lastGazePoint.x - this.calibrationCenter.x), -(this.lastGazePoint.y - this.calibrationCenter.y));
        finished();
    }

    private void initGUI() {
        setVisible(true);
        setOpaque(false);
        setBorder(new LineBorder(new Color(1f, 1f, 1f, 0.2f)));
    }
}
