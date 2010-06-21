/*
 * DashboardFrame.java
 *
 * Copyright (c) 2007, Ralf Biedert, DFKI. All rights reserved.
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
package de.dfki.km.text20.browserplugin.services.onscreenmenu.impl;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.java.javafx.ui.StackLayout;
import de.dfki.km.text20.browserplugin.services.onscreenmenu.impl.util.JBackgroundPanel;
import de.dfki.km.text20.browserplugin.services.onscreenmenu.impl.util.JShadePanel;

/**
 * @author rb
 *
 */
public class DashboardFrame extends JFrame {

    /** */
    private static final long serialVersionUID = 6405434439072109638L;

    /** */
    JBackgroundPanel backgroundPanel;

    /** */
    JPanel contentPane = new JPanel();

    /** */
    List<Point> lastPoints = new ArrayList<Point>();

    Robot robot;

    /** */
    JShadePanel shadePanel;

    /**
     * 
     */
    public DashboardFrame() {
        try {
            this.robot = new Robot();
        } catch (final AWTException e) {
            e.printStackTrace();
        }
        initGUI();
    }

    /**
     * 
     * @param point
     */
    public void addGazePoint(final Point point) {
        synchronized (this.lastPoints) {
            this.lastPoints.add(point);
            if (this.lastPoints.size() > 10) {
                this.lastPoints.remove(0);
            }
        }
        repaint();
    }

    /* (non-Javadoc)
     * @see java.awt.Container#paint(java.awt.Graphics)
     */

    /**
     * @return .
     */
    public JPanel getFrameContainer() {
        return this.contentPane;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#setVisible(boolean)
     */

    @Override
    public void paint(final Graphics g) {
        super.paint(g);

        List<Point> gazePoints = null;

        synchronized (this.lastPoints) {
            gazePoints = new ArrayList<Point>(this.lastPoints);
        }

        g.setColor(new Color(1f, 0f, 0f, 0.5f));
        for (final Point point : gazePoints) {
            g.drawOval(point.x - 1, point.y - 1, 2, 2);
        }
    }

    @Override
    public void setVisible(final boolean b) {
        if (b == isVisible()) return;

        if (b) {
            fitToScreen();
        }

        super.setVisible(b);
    }

    private void initGUI() {
        DashboardUtilities.invokeAndWait(new Runnable() {

            public void run() {

                DashboardFrame.this.backgroundPanel = new JBackgroundPanel();
                DashboardFrame.this.shadePanel = new JShadePanel();
                setUndecorated(true);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                DashboardFrame.this.contentPane.setOpaque(false);
                DashboardFrame.this.contentPane.setLayout(null);
                DashboardFrame.this.contentPane.setBackground(new Color(0f, 0f, 0f, 0f));

                rebuildDashboardStack();
            }

        });

    }

    /**
     * Makes the dashboard visible and fits it to the screen.
     */
    void fitToScreen() {

        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = defaultToolkit.getScreenSize();

        // We need this priviledged stuff for applets...
        final BufferedImage background = AccessController.doPrivileged(new PrivilegedAction<BufferedImage>() {

            public BufferedImage run() {
                return DashboardFrame.this.robot.createScreenCapture(new Rectangle(screenSize));
            }
        });

        this.backgroundPanel.setBackground(background);

        setSize(screenSize);
        setLocation(0, 0);
    }

    void rebuildDashboardStack() {
        final Container c = getContentPane();

        // Clear everything, then build up again
        c.removeAll();

        // First add all our lowlevel panel
        c.setLayout(new StackLayout());
        c.add(this.backgroundPanel, StackLayout.TOP);
        c.add(this.shadePanel, StackLayout.TOP);
        c.add(this.contentPane, StackLayout.TOP);

        validate();
        repaint();
    }
}
