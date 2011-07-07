/*
 * TestFrameworkManager.java
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
package de.dfki.km.text20.sandbox.misc;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.util.JSPFProperties;
import sun.applet.AppletSecurity;
import de.dfki.km.text20.browserplugin.browser.browserplugin.impl.FrameworkManager;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;

/**
 * @author Ralf Biedert
 *
 */
public class TestFrameworkManager {

    private PluginManager pluginManager;
    private SessionRecorder sessionRecorder;
    @SuppressWarnings("unused")
    private TrackingDeviceManager deviceManager;

    /**
     *
     */
    public TestFrameworkManager() {
        System.setSecurityManager(new AppletSecurity());

        this.pluginManager = new FrameworkManager(new JSPFProperties()).getPluginManager();
        this.sessionRecorder = this.pluginManager.getPlugin(SessionRecorderManager.class).createSessionRecorder();
        this.deviceManager = this.pluginManager.getPlugin(TrackingDeviceManager.class);

        System.out.println(this.sessionRecorder);
        this.pluginManager.shutdown();

    }

    /**
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        new TestFrameworkManager();
    }

}
