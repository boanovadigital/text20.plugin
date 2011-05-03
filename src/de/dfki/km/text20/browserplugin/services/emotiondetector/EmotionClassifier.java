/*
 * EmotionClassifier.java
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
package de.dfki.km.text20.browserplugin.services.emotiondetector;

/**
 * Returns the current emotions. 
 * 
 * @author Farida Ismail
 * @since 1.3
 */
@Deprecated
public interface EmotionClassifier {
    /**
     * Returns the current emotion, which is either <code>happy</code>, <code>doubt</code>, <code>interested</code> or <code>boredom</code>.
     *
     * @return A String with the currently detected emotion.
     */
    @Deprecated
    public String getEmotion();
}
