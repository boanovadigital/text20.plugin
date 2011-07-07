/*
 * FixationDummy.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.util;

import java.io.Serializable;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;

/**
 * Allows you to quickly create a {@link Saccade} object where one is needed. 
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class SaccadeDummy implements Saccade, Cloneable, Serializable {
    /** */
    private static final long serialVersionUID = 5907919169786728874L;

    /** Start fixation */
    public Fixation start = null;
    
    /** End fixation */
    public Fixation end = null;

    /**
     * Creates a {@link Saccade} dummy.
     */
    public SaccadeDummy() {}

    /**
     * Creates a {@link Saccade} dummy with the given start and end fixation.
     * @param start The start fixation.
     * @param end The end fixation.
     */
    public SaccadeDummy(Fixation start, Fixation end) {
        this.start = start;
        this.end = end;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Saccade (" + this.start + " -> " + this.end + ")";
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade#getEnd()
     */
    @Override
    public Fixation getEnd() {
        return this.end;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade#getStart()
     */
    @Override
    public Fixation getStart() {
        return this.start;
    }
}
