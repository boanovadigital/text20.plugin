/*
 * SessionRecorderExtensions.java
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
package de.dfki.km.text20.browserplugin.extensions.qc.sessionrecorder;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.injections.InjectPlugin;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.InformationListener;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.SessionRecorderItem;
import de.dfki.km.text20.browserplugin.services.extensionmanager.Extension;
import de.dfki.km.text20.browserplugin.services.extensionmanager.annotations.ExtensionMethod;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;

/**
 * @author Ralf Biedert
 * 
 */
@PluginImplementation
public class SessionRecorderExtensions implements Extension {

    /** */
    SessionRecorder sessionRecorder;

    /** */
    @InjectPlugin
    public InformationBroker broker;

    
    @ExtensionMethod
    public void markLog(String mark) {
        if(this.sessionRecorder == null) return;
        this.sessionRecorder.markLog(mark);  
    }

    
    @ExtensionMethod
    public void takeScreenshot() {
        if(this.sessionRecorder == null) return;
        this.sessionRecorder.takeScreenshot();  
    }
    
    @ExtensionMethod
    public void mouseClicked(int type, int button) {
        if(this.sessionRecorder == null) return;
        this.sessionRecorder.mouseClicked(type, button);
    }


    /** Called upon init */
    @Init
    public void init() {
        this.broker.subscribe(SessionRecorderItem.class, new InformationListener<SessionRecorder>() {
            @Override
            public void update(SessionRecorder arg0) {
                SessionRecorderExtensions.this.sessionRecorder = arg0;
            }
        });
    }
}
