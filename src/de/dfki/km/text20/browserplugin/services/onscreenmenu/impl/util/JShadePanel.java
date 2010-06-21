/*
 * JShadePanel.java
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
package de.dfki.km.text20.browserplugin.services.onscreenmenu.impl.util;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * @author rb
 *
 */
public class JShadePanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 6998808060247517766L;

    /**
     * 
     */
    public JShadePanel() {
        setOpaque(false);
    }

    @Override
    public void paint(final Graphics g) {
        //Color background = new Color(0.0f, 0.0f, 0.0f, 0.00f);
        final Color background = new Color(0.0f, 0.0f, 0.0f, 0.5f);
        //Color background = new Color(0.0f, 0.0f, 0.0f, 1.0f);
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}