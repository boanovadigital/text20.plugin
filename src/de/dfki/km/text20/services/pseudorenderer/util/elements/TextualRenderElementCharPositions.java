/*
 * TextualRenderElementCharPositions.java
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
package de.dfki.km.text20.services.pseudorenderer.util.elements;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * Calculates char positions of render elements.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class TextualRenderElementCharPositions {

    /** Used to measure sized */
    private final FontMetrics fontMetrics;

    /** To dispose graphics */
    private final Graphics graphics;

    /**
     * 
     */
    public TextualRenderElementCharPositions() {
        final BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);

        this.graphics = bufferedImage.getGraphics();
        this.fontMetrics = this.graphics.getFontMetrics();

    }

    /**
     * Returns the position rectangle of the requested character.
     * 
     * @param renderElement
     * @param type
     * @param chatPosition
     * 
     * @return .
     */
    public Rectangle getCoordinatesOf(final TextualRenderElement renderElement,
                                      final CoordinatesType type, final int chatPosition) {

        final String content = renderElement.getContent();
        final Rectangle geometry = renderElement.getGeometry(type);

        final double widthOfRectangle = geometry.getWidth();

        double startPosChar = 0;
        double endPosChar = 0;
        double currentSum = 0;

        for (int i = 0; i < content.length(); i++) {
            final char c = content.charAt(i);

            final double cWidth = this.fontMetrics.charWidth(c);

            if (i == chatPosition) {
                startPosChar = currentSum;
                endPosChar = currentSum + cWidth;
            }

            currentSum += cWidth;
        }

        final double _start = startPosChar / currentSum;
        final double _end = endPosChar / currentSum;

        final int start = (int) (_start * widthOfRectangle);
        final int end = (int) (_end * widthOfRectangle);

        final Rectangle rval = (Rectangle) geometry.clone();
        rval.x += start;
        rval.width = end - start;

        return rval;
    }

    /**
     * @param renderElement
     * @param type
     * @param p
     * 
     * @return the character position at the word under gaze.
     */
    public int getPositionOf(final TextualRenderElement renderElement,
                             final CoordinatesType type, final Point p) {

        // Check if we have actually hit the element
        final Rectangle geometry = renderElement.getGeometry(type);
        if (!geometry.contains(p)) return -1;

        final String content = renderElement.getContent();
        final int x = p.x - geometry.x;
        final double px = x / (double) geometry.width;

        double last = 0;

        //
        for (int i = 0; i < content.length(); i++) {
            final String a = content.substring(0, i + 1);
            final double aa = getWidthRelation(a, content);

            if (last <= px && px <= aa) return i;

            last = aa;
        }

        return -1;
    }

    /**
     * Returns the width of a character. 
     * 
     * @param c
     * @param d 
     * @return .
     */
    public double getWidthRelation(final char c, final char d) {
        final double a = this.fontMetrics.charWidth(c);
        final double b = this.fontMetrics.charWidth(d);
        return a / b;
    }

    /**
     * @param a
     * @param b
     * @return .
     */
    public double getWidthRelation(final String a, final String b) {

        double aa = 0;
        double bb = 0;

        for (final char c : a.toCharArray()) {
            aa += this.fontMetrics.charWidth(c);
        }
        for (final char c : b.toCharArray()) {
            bb += this.fontMetrics.charWidth(c);
        }

        return aa / bb;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        // Don't know if that's really necessary.
        this.graphics.dispose();
    }
}
