/*
 * OverlayFrame.java
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
package de.dfki.km.text20.browserplugin.extensions.qc.diagnosticoverlay;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import net.jcores.jre.interfaces.java.KeyStroke;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.util.EyeTrackingEventDummy;

/**
 * @author Ralf Biedert
 */
public class OverlayFrame extends JFrame {
    /** */
    private static final long serialVersionUID = -4574172025569108274L;

    /** */
    private Robot robot;

    /** */
    private Toolkit toolkit;

    /** */
    private BufferedImage image;

    /** */
    private final LinkedList<EyeTrackingEvent> events = new LinkedList<EyeTrackingEvent>();

    /** */
    private volatile Fixation fixation = null;

    /** */
    public OverlayFrame() {
        this.robot = $(Robot.class).spawn().get(0);
        this.toolkit = Toolkit.getDefaultToolkit();

        setLayout(null);
        setResizable(false);
        setUndecorated(true);
        setSize(this.toolkit.getScreenSize());

        $(this).keypress(KeyEvent.VK_ESCAPE, new KeyStroke() {
            @Override
            public void keystroke(KeyEvent arg0) {
                setVisible(false);
            }
        });
    }

    /** Activates the overlay. */
    public void activate() {
        if (this.robot == null) return;

        final Dimension size = this.toolkit.getScreenSize();

        this.image = this.robot.createScreenCapture(new Rectangle(0, 0, size.width, size.height));

        setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        toFront();
        requestFocus();
        setBounds(0, 0, size.width, size.height);
    }

    /**
     * @param event
     */
    public void trackingEvent(EyeTrackingEvent event) {
        if (!isVisible()) return;

        // Update the list
        synchronized (this.events) {
            this.events.add(event);
            if (this.events.size() > 5) this.events.remove(0);
        }
        
        repaint(50, 0, 0, getWidth(), getHeight());
    }
    
    /**
     * Sets the current fixation
     * 
     * @param f
     */
    public void fixation(Fixation f) {
        this.fixation = f;
        
        repaint(50, 0, 0, getWidth(), getHeight());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Window#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.image, 0, 0, null);

        // Draw the raw data
        synchronized (this.events) {
            g.setColor(Color.BLACK);
            for (EyeTrackingEvent event: this.events) {
                final Point p = event.getGazeCenter();
                g.fillOval(p.x - 2, p.y - 2, 4, 4);
            }
        }
        
        
        // Draw the fixation
        if(this.fixation != null) {
            final Point p = this.fixation.getCenter();
            g.setColor(Color.RED);
            g.drawOval(p.x - 10, p.y - 10, 20, 20);

        }
    }

    /**
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        OverlayFrame frame = new OverlayFrame();
        frame.activate();
        
        for(int i = 0; i<1000; i++) {
            EyeTrackingEventDummy dummy = new EyeTrackingEventDummy(new Dimension(1000, 400));
            dummy.simulate(new Point($.random().nextInt(1000), $.random().nextInt(1000)));
            frame.trackingEvent(dummy);
            Thread.sleep(1);
        }
    }

  
}
