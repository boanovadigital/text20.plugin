/*
 * ExtensionManagerUtil.java
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
package de.dfki.km.text20.browserplugin.services.extensionmanager.util;

import static net.jcores.CoreKeeper.$;

import java.util.List;

import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;

/**
 * @author Ralf Biedert
 */
public class ExtensionManagerUtil implements ExtensionManager {
    /** */
    private ExtensionManager manager;

    /**
     * @param manager
     */
    public ExtensionManagerUtil(ExtensionManager manager) {
        this.manager = manager;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager#executeFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object executeFunction(String function, String args) {
        return this.manager.executeFunction(function, args);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager#executeFunction(java.lang.String, java.lang.String)
     */
    public Object executeFunction(String function, String... args) {
        return this.manager.executeFunction(function, $(args).encode().join(","));
    }
    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager#executeFunction(java.lang.String, java.lang.String)
     */
    public Object executeFunction(String function, Object... args) {
        return this.manager.executeFunction(function, $(args).string().encode().join(","));
    }

    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager#getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        return this.manager.getExtensions();
    }
    
}
