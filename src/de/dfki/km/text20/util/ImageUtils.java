/*
 * ImageUtils.java
 *
 * Copyright (c) 2008, Ralf Biedert, DFKI. All rights reserved.
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
