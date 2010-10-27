/*
 * ImageUtils.java
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
package de.dfki.km.text20.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author rb
 *
 */
public class ImageUtils {

    /**
     * Code shamelessly stolen from the net.
     *
     * @param image
     * @param scale
     * @return .
     */
    public static BufferedImage getScaledImage(final Image image, final double scale) {
        final int w = (int) (scale * image.getWidth(null));
        final int h = (int) (scale * image.getHeight(null));
        final int type = BufferedImage.TYPE_INT_RGB;
        final BufferedImage out = new BufferedImage(w, h, type);
        final Graphics2D g2 = out.createGraphics();
        final AffineTransform at = AffineTransform.getScaleInstance(scale, scale);
        g2.drawImage(image, at, null);
        g2.dispose();
        return out;
    }

    /**
     * @param image
     * @param width
     * @return .
     */
    public static BufferedImage getScaledImage(final Image image, final int width) {
        final float scale = (float) width / image.getWidth(null);

        return getScaledImage(image, scale);
    }

    /**
     * @param image
     * @return .
     */
    public static boolean isBlack(final BufferedImage image) {

        final WritableRaster raster = image.getRaster();
        final int w = raster.getWidth();
        final int h = raster.getHeight();

        final double d[] = new double[4];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                raster.getPixel(x, y, d);

                final double q = d[0] + d[1] + d[2];
                if (q > 0) return false;
            }
        }

        return true;
    }

}
