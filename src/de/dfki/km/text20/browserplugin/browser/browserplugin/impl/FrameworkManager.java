/*
 * FrameworkManager.java
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
package de.dfki.km.text20.browserplugin.browser.browserplugin.impl;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.JSPFProperties;
import net.xeoh.plugins.base.util.uri.ClassURI;
import net.xeoh.plugins.meta.updatecheck.impl.UpdateCheckImpl;
import net.xeoh.plugins.remote.impl.lipermi.RemoteAPIImpl;
import net.xeoh.plugins.remotediscovery.impl.v4.RemoteDiscoveryImpl;
import de.dfki.km.text20.browserplugin.extensions.backgroundservices.BackgroundServicesExtension;
import de.dfki.km.text20.browserplugin.extensions.brainz.BrainTrackingExtension;
import de.dfki.km.text20.browserplugin.extensions.discovery.DiscoveryExtension;
import de.dfki.km.text20.browserplugin.extensions.hacks.VariousHacksExtension;
import de.dfki.km.text20.browserplugin.extensions.sessionrecorder.SessionRecorderExtensions;
import de.dfki.km.text20.browserplugin.extensions.speechio.SpeechIOExtension;
import de.dfki.km.text20.browserplugin.services.devicemanager.impl.TrackingDeviceManagerImpl;
import de.dfki.km.text20.browserplugin.services.extensionmanager.impl.ExtensionManagerImpl;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.impl.MasterGazeHandlerManagerImpl;
import de.dfki.km.text20.browserplugin.services.pagemanager.impl.PageManagerManagerImpl;
import de.dfki.km.text20.browserplugin.services.persistentpreferences.impl.PersistentPreferencesImpl;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.SessionRecorderManagerImpl;
import de.dfki.km.text20.services.evaluators.gaze.impl.GazeEvaluatorManagerImpl;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.fixation.v1.FixationHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.fixationline.v4.FixationLineHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.perusal.v3.PerusalHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.raw.v1.RawHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.saccade.v1.SaccadeHandlerFactory;
import de.dfki.km.text20.services.evaluators.gaze.impl.handler.weaksaccade.v2.WeakSaccadeHandlerFactory;
import de.dfki.km.text20.services.pseudorenderer.impl.PseudorendererManagerImpl;
import de.dfki.km.text20.services.trackingdevices.brain.impl.braintrackingserver.BrainTrackingServerDeviceProviderImpl;
import de.dfki.km.text20.services.trackingdevices.eyes.impl.mouse.MouseTrackingDeviceProviderImpl;
import de.dfki.km.text20.services.trackingdevices.eyes.impl.trackingserver.TrackingServerDeviceProviderImpl;

/**
 * 
 * @author rb
 *
 */
public class FrameworkManager {
    /**
     * Pluginmanager, the heart of everything.
     */
    private PluginManager pluginManager;

    /**
     * @param props 
     * 
     */
    public FrameworkManager(JSPFProperties props) {
        initPluginFramework(props);
    }

    /**
     * Return ths plugin manager
     * @return .
     */
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    /**
     * Init the plugin framework
     * @param props 
     */
    private void initPluginFramework(JSPFProperties props) {

        this.pluginManager = PluginManagerFactory.createPluginManager(props);

        // Manually load the plugins. Not really beautiful ...

        this.pluginManager.addPluginsFrom(new ClassURI(RemoteAPIImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(RemoteDiscoveryImpl.class).toURI());

        this.pluginManager.addPluginsFrom(new ClassURI(MouseTrackingDeviceProviderImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(TrackingServerDeviceProviderImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(BrainTrackingServerDeviceProviderImpl.class).toURI());

        this.pluginManager.addPluginsFrom(new ClassURI(PseudorendererManagerImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(GazeEvaluatorManagerImpl.class).toURI());

        this.pluginManager.addPluginsFrom(new ClassURI(PersistentPreferencesImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(SessionRecorderManagerImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(MasterGazeHandlerManagerImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(PageManagerManagerImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(TrackingDeviceManagerImpl.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(ExtensionManagerImpl.class).toURI());

        // Add current handler ...
        this.pluginManager.addPluginsFrom(new ClassURI(FixationHandlerFactory.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(FixationLineHandlerFactory.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(PerusalHandlerFactory.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(RawHandlerFactory.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(SaccadeHandlerFactory.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(WeakSaccadeHandlerFactory.class).toURI());

        // Add extensions ...
        this.pluginManager.addPluginsFrom(new ClassURI(DiscoveryExtension.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(BackgroundServicesExtension.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(VariousHacksExtension.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(SessionRecorderExtensions.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(SpeechIOExtension.class).toURI());
        this.pluginManager.addPluginsFrom(new ClassURI(BrainTrackingExtension.class).toURI());

        // Register additional modules
        this.pluginManager.addPluginsFrom(new ClassURI(UpdateCheckImpl.class).toURI());
    }
}
