/*
 * OptionSlowMotion.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;

/**
 * Tells the {@link SessionReplay} to replay slowly.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class OptionSlowMotion implements ReplayOption {
    /**  */
    private static final long serialVersionUID = 4224184820549735162L;
    
    /**  */
    private int factor = 1;

    /**
     * Constructs a slow motion object.
     * 
     * @param factor Stretch factor for replay, <code>1</code> means realtime, <code>2</code> means twice the time, ...
     */
    public OptionSlowMotion(int factor) {
        this.factor = factor;
    }

    /**
     * Returns the factor.
     * 
     * @return The factor.
     */
    public int getFactor() {
        return this.factor;
    }
}
