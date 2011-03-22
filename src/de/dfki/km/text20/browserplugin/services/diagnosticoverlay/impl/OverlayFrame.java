/*
 * OverlayFrame.java
 * 
 * Copyright (c) 2011, Ralf Biedert All rights reserved.
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
package de.dfki.km.text20.browserplugin.services.diagnosticoverlay.impl;

import static net.jcores.CoreKeeper.$;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import net.jcores.interfaces.java.KeyStroke;

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
    
    /** */
    public void activate() {
        if(this.robot == null) return;
        
        final Dimension size = this.toolkit.getScreenSize();
        
        this.image = this.robot.createScreenCapture(new Rectangle(0, 0, size.width, size.height));
        
        setVisible(true);
        setBounds(0, 0, size.width, size.height);
    }
    
    
    /* (non-Javadoc)
     * @see java.awt.Window#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(this.image, 0, 0, null);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        OverlayFrame frame = new OverlayFrame();
        frame.activate();
    }
}
