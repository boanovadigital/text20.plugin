/*
 * SessionReplayUtil.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.util;

import java.awt.Dimension;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.jcores.jre.utils.VanillaUtil;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * Contains various helper functions for session replays.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class SessionReplayUtil extends VanillaUtil<SessionReplay> implements SessionReplay {

    /**
     * Constructs a new session replay util.
     * 
     * @param replay The replay to wrap.
     */
    public SessionReplayUtil(SessionReplay replay) {
        super(replay);
    }
    
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#replay(de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener, de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption[])
     */
    @Override
    public void replay(ReplayListener listener, ReplayOption... options) {
        this.object.replay(listener, options);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getScreenSize()
     */
    @Override
    public Dimension getScreenSize() {
        return this.object.getScreenSize();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getProperties(java.lang.String[])
     */
    @Override
    public Map<String, String> getProperties(String... properties) {
        return this.object.getProperties(properties);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getDisplacements()
     */
    @SuppressWarnings("deprecation")
    @Override
    public List<DisplacementRegion> getDisplacements() {
        return this.object.getDisplacements();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getResource(java.lang.String)
     */
    @Override
    public InputStream getResource(String resource) {
        return this.object.getResource(resource);
    }
    
    /**
     * Returns a fake eye tracking device for this replay.  
     */
    public void getFakeEyeTrackingDevice() {
        // TODO
    }
    
    /**
     * Returns a fake brain tracking device. 
     */
    public void getFakeBrainTrackingDevice() {
        // TODO
    }
}
