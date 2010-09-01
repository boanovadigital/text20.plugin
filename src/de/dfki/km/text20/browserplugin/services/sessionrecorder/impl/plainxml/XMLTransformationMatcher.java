/*
 * MyMatcher.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

/**
 * @author rb
 *
 */
public class XMLTransformationMatcher implements Matcher {

    @SuppressWarnings("unchecked")
    public Transform<?> match(final Class arg0) throws Exception {
        if (arg0.equals(Rectangle.class)) return new Transform<Rectangle>() {

            public Rectangle read(final String s) throws Exception {
                final String[] split = s.split(",");
                final int x = Integer.parseInt(split[0]);
                final int y = Integer.parseInt(split[1]);
                final int w = Integer.parseInt(split[2]);
                final int h = Integer.parseInt(split[3]);

                return new Rectangle(x, y, w, h);
            }

            public String write(final Rectangle r) throws Exception {
                return r.x + "," + r.y + "," + r.width + "," + r.height;
            }

        };

        if (arg0.equals(Dimension.class)) return new Transform<Dimension>() {

            public Dimension read(final String s) throws Exception {
                final String[] split = s.split(",");
                final int w = Integer.parseInt(split[0]);
                final int h = Integer.parseInt(split[1]);

                return new Dimension(w, h);
            }

            public String write(final Dimension r) throws Exception {
                return r.width + "," + r.height;
            }

        };

        if (arg0.equals(Point.class)) return new Transform<Point>() {

            public Point read(final String s) throws Exception {
                final String[] split = s.split(",");
                final int x = Integer.parseInt(split[0]);
                final int y = Integer.parseInt(split[1]);

                return new Point(x, y);
            }

            public String write(final Point r) throws Exception {
                return r.x + "," + r.y;
            }

        };

        return null;
    }
}