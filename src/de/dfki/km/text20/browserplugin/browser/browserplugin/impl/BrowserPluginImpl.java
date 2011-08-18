/*
 * BrowserPluginImpl.java
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

import static net.jcores.jre.CoreKeeper.$;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.jcores.jre.interfaces.functions.F1;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.util.JSPFProperties;
import net.xeoh.plugins.diagnosis.local.Diagnosis;
import net.xeoh.plugins.diagnosis.local.DiagnosisChannel;
import net.xeoh.plugins.diagnosis.local.options.status.OptionInfo;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.meta.updatecheck.UpdateCheck;
import net.xeoh.plugins.remote.RemoteAPI;
import net.xeoh.plugins.remotediscovery.RemoteDiscovery;
import netscape.javascript.JSObject;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.configuration.OptionFixationParametersItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.configuration.SessionDirectoryItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.configuration.TransmissionModeItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.meta.BuildNumberItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.BrowserAPIItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.GazeEvaluatorItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.JavaScriptExecutorItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.MasterGazeHandlerItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.PageManagerItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.PseudorendererItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.brokeritems.services.SessionRecorderItem;
import de.dfki.km.text20.browserplugin.browser.browserplugin.diagnosis.channels.tracing.BrowserPluginTracer;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandlerManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManagerManager;
import de.dfki.km.text20.browserplugin.services.persistentpreferences.PersistentPreferences;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;
import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluator;
import de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener.OptionFixationParameters;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererManager;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;

/**
 * Will be instantiated by the browser (is a Java Applet). This is the main entry point 
 * for the Text 2.0 Framework when it runs as a browser plugin.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class BrowserPluginImpl extends Applet implements JSExecutor, BrowserAPI {
    /** Indicates how JavaScript calls should be made from the Applet. */
    public static enum TransmitMode {
        ASYNC, DIRECT
    }

    /** The build number of this release */
    private final String buildNumber;
    
    /** */
    private static final long serialVersionUID = 8654743028251010225L;

    /** Appened to all live-connect callbacks */
    private String callbackPrefix;

    /** The value to transmit to the updatecheck plugin */
    private String updatecheck;

    /** Keeps reference to the tracking device */
    private TrackingDeviceManager deviceManager;

    /** Handles tracking events. */
    private MasterGazeHandler gazeHandler;

    /** Evaluate raw gaze events. */
    private GazeEvaluator gazeEvaluator;

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    private InformationBroker infoBroker;

    /** Manages the related webpage */
    private PageManager pageManager;

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    PluginManager pluginManager;

    /** Used to store persistent prefrences. */
    private PersistentPreferences preferences;

    /** The pseudorenderer reflects the current page's status */
    private Pseudorenderer pseudorender;

    /** Keeps track of user defined extensions */
    private ExtensionManager extensionManager;

    /** The initialized eye tracking device */
    private EyeTrackingDevice eyeTrackingDevice;

    /** The initialized brain tracking device */
    private BrainTrackingDevice brainTrackingDevice;

    /** Responsible for tracing messages */
    private DiagnosisChannel<String> diagnosis;

    /** Instance id if this plugin */
    final int instanceID = new Random().nextInt();

    /** Handles calls with many objects */
    final BatchHandler batchHandler = new BatchHandler(this);

    /** If the diagnosis should be enabled */
    boolean diagnosisEnabled;

    /** Master file path */
    String masterFilePath = "/tmp";

    /** If set, getParameter will use this object to return values */
    Map<String, String> parameterOverride = null;

    /** Records screenshots and gaze points */
    SessionRecorder sessionRecorder;

    /** Executor to call javascript */
    final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /** If we should keep a session record */
    boolean recordingEnabled = true;

    /** How to call JavaScript */
    TransmitMode transmitMode = TransmitMode.DIRECT;

    /** Browser window */
    JSObject window;

    private Thread mouseThread;

    public BrowserPluginImpl() {
        this.buildNumber = $(BrowserPluginImpl.class.getResourceAsStream("browser.plugin.version")).text().split("\n").hashmap().get("build");
        System.out.println();
        System.out.println("BrowserPluginImpl.new() -- Build " + this.buildNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#batch(java
     * .lang.String)
     */
    @Override
    public void batch(final String call) {
        this.batchHandler.batch(call);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#callFunction
     * (java.lang.String, java.lang.String)
     */
    @Override
    public Object callFunction(final String function) {
        this.diagnosis.status("callfunction/start", new OptionInfo("function", function));

        try {
            if (this.sessionRecorder != null) {
                this.sessionRecorder.callFunction(function);
            }

            final int indexOf = function.indexOf("(");
            final String name = function.substring(0, indexOf);

            String args = function.substring(indexOf + 1);
            args = args.substring(0, args.lastIndexOf(')'));

            this.diagnosis.status("callfunction/function", new OptionInfo("name", name), new OptionInfo("args", args));

            // Execute the proper extension ...
            if (this.extensionManager.getExtensions().contains(name)) {
                this.diagnosis.status("callfunction/extension/call");
                final Object rval = this.extensionManager.executeFunction(name, args);
                
                this.diagnosis.status("callfunction/end", new OptionInfo("rval", $(rval).get("null").toString()));
                return rval;
            }

            this.diagnosis.status("callfunction/end/noextension");
            return null;

        } catch (final Exception e) {
            this.diagnosis.status("callfunction/exception", new OptionInfo("message", e.getMessage()));              
            e.printStackTrace();
        }
        
        this.diagnosis.status("callfunction/end/unusual");
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#destroy()
     */
    @Override
    public void destroy() {
        System.out.println("BrowserPluginImpl.destroy()");
        this.diagnosis.status("destroy/call");
        this.pluginManager.shutdown();
    }

    /**
     * Execute a script inside the browser
     * 
     * (non-Javadoc)
     * 
     * @see de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor#executeJSFunction(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public Object executeJSFunction(final String _function, final Object... args) {
        this.diagnosis.status("executejsfunction/start", new OptionInfo("function", _function), new OptionInfo("args", $(args).string().join(",")));

        // Append the callback prefix to the function.
        final String function = this.callbackPrefix + _function;
        
        this.diagnosis.status("executejsfunction/function", new OptionInfo("function", function));

        tryGetWindow();

        this.sessionRecorder.executeJSFunction(function, args);

        // If the window is still null, we try our fallback solution (maybe slow ...)
        if (this.window == null) {
            this.diagnosis.status("executejsfunction/call/nowindow");

            int ctr = 0;

            final StringBuilder sb = new StringBuilder();
            sb.append("javascript:");
            sb.append(function);
            sb.append("(");
            for (final Object object : args) {
                sb.append("'");
                sb.append(object);
                sb.append("'");

                if (ctr++ < args.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(");");

            this.diagnosis.status("executejsfunction/call/nowindow/call", new OptionInfo("call", sb.toString()));

            try {
                final AppletContext appletContext = getAppletContext();
                appletContext.showDocument(new URL(sb.toString()));
            } catch (final MalformedURLException e) {
                this.diagnosis.status("executejsfunction/call/nowindow/exception", new OptionInfo("message", e.getMessage()));              
                e.printStackTrace();
            }
            
            this.diagnosis.status("executejsfunction/end");
            return null;
        }

        // This is the ugly way (Safari likes it)
        if (this.transmitMode.equals(TransmitMode.ASYNC)) {
            this.diagnosis.status("executejsfunction/call/async");
            this.singleThreadExecutor.execute(new Runnable() {
                @SuppressWarnings("synthetic-access")
                @Override
                public void run() {
                    try {
                        BrowserPluginImpl.this.diagnosis.status("executejsfunction/call/async/executor");
                        BrowserPluginImpl.this.window.call(function, args);
                    } catch (final Exception e) {
                        BrowserPluginImpl.this.diagnosis.status("executejsfunction/call/async/executor/exception", new OptionInfo("message", e.getMessage()));
                        e.printStackTrace();
                    }
                }
            });
            
            this.diagnosis.status("executejsfunction/end");
            return null;
        }

        // This is the nice way (Firefox likes it)
        if (this.transmitMode.equals(TransmitMode.DIRECT)) {
            this.diagnosis.status("executejsfunction/call/direct");            
            try {
                this.diagnosis.status("executejsfunction/end");            
                return this.window.call(function, args);
            } catch (final Exception e) {
                this.diagnosis.status("executejsfunction/call/direct/exception", new OptionInfo("message", e.getMessage()));              
                e.printStackTrace();
            }
        }

        this.diagnosis.status("executejsfunction/end/unusual");              
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String key) {
        if (this.parameterOverride == null) { return super.getParameter(key); }

        return this.parameterOverride.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#getParameterInfo()
     */
    @Override
    public String[][] getParameterInfo() {
        return new String[][] { { "transmitmode", "string", "What to use to call JavaScript" }, { "trackingdevice", "string", "Identifies the device handler" }, { "trackingconnection", "url", "If it is a remote device, where to contact?" }, { "sessionpath", "string", "Save all stuff to what?" }, { "callbackprefix", "string", "Appended to all callbacks from the applet via liveconnect." } };
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#getPreference
     * (java.lang.String, java.lang.String)
     */
    @Override
    public String getPreference(final String key, final String deflt) {
        this.diagnosis.status("getpreference/start", new OptionInfo("key", key), new OptionInfo("default", deflt));
        this.sessionRecorder.getPreference(key, deflt);
        final String rval = this.preferences.getString(key, deflt);
        this.diagnosis.status("getpreference/end", new OptionInfo("key", key), new OptionInfo("rval", rval));
        return rval;
    }

    /**
     * Wird seltener aufgerufen als start, sollte hier Verbindung mit dem Eye-Tracker-
     * Server aufnehmen und das Pluginframework initialisieren.
     */
    @Override
    public void init() {
        System.out.println("BrowserPluginImpl.init()");
        // Get all parameters we need for JSPF initialization
        processBootstrapParameters();

        final JSPFProperties props = new JSPFProperties();
        props.setProperty(RemoteAPI.class, "proxy.timeout", "1000");
        props.setProperty(RemoteDiscovery.class, "startup.locktime", "1000");
        props.setProperty(Diagnosis.class, "recording.enabled", Boolean.toString(this.diagnosisEnabled));
        props.setProperty(Diagnosis.class, "recording.file", this.masterFilePath + "/diagnosis.record");
        props.setProperty(Diagnosis.class, "recording.format", "java/serialization");
        props.setProperty(Diagnosis.class, "analysis.stacktraces.enabled", "false");
        props.setProperty(Diagnosis.class, "analysis.stacktraces.depth", "10000");
        props.setProperty(UpdateCheck.class, "update.url", "http://api.text20.net/common/versioncheck/");
        props.setProperty(UpdateCheck.class, "update.enabled", this.updatecheck);
        props.setProperty(UpdateCheck.class, "product.name", "text20.plugin");
        props.setProperty(UpdateCheck.class, "product.version", "1.4.1"); // TODO: Get this
                                                                        // version number
                                                                        // from a better
                                                                        // place!

        this.pluginManager = new FrameworkManager(props).getPluginManager();

        this.extensionManager = this.pluginManager.getPlugin(ExtensionManager.class);
        this.sessionRecorder = this.pluginManager.getPlugin(SessionRecorderManager.class).createSessionRecorder();
        this.deviceManager = this.pluginManager.getPlugin(TrackingDeviceManager.class);
        this.infoBroker = this.pluginManager.getPlugin(InformationBroker.class);
        this.preferences = this.pluginManager.getPlugin(PersistentPreferences.class);
        this.pseudorender = this.pluginManager.getPlugin(PseudorendererManager.class).createPseudorenderer();
        this.gazeHandler = this.pluginManager.getPlugin(MasterGazeHandlerManager.class).createMasterGazeHandler(this, this.pseudorender);
        this.pageManager = this.pluginManager.getPlugin(PageManagerManager.class).createPageManager(this.pseudorender);
        this.diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(BrowserPluginTracer.class);

        
        this.diagnosis.status("init/start/late");

        // Store parameters
        storeParameters();

        // Evaluate additional parameter
        processAdditionalParameters();

        this.diagnosis.status("init/init/devices");

        // Setup gaze recording
        initTrackingDevice();

        this.diagnosis.status("init/init/mouse");

        // Setup mouse recording
        initMouseRecording();

        // Try to get the window
        tryGetWindow();

        this.diagnosis.status("init/set/evaluator");

        // Provide the tracking device to other listeners.
        this.gazeHandler.setTrackingDevice(this.eyeTrackingDevice);
        this.gazeEvaluator = this.gazeHandler.getGazeEvaluator();

        this.diagnosis.status("init/publish");

        // Publish items of the information broker
        publishBrokerItems();

        this.diagnosis.status("init/call/javascript");

        tellJSStatus("INITIALIZED", this.buildNumber);

        this.diagnosis.status("init/end");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * registerListener(java.lang.String, java.lang.String)
     */
    @Override
    public void registerListener(final String type, final String listener) {
        this.diagnosis.status("registerlistener/call", new OptionInfo("type", type), new OptionInfo("listener", listener));
        this.sessionRecorder.registerListener(type, listener);
        this.gazeHandler.registerJSCallback(type, listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * removeListener(java.lang.String)
     */
    @Override
    public void removeListener(final String listener) {
        this.diagnosis.status("removelistener/call", new OptionInfo("listener", listener));
        this.sessionRecorder.removeListener(listener);
        this.gazeHandler.removeJSCallback(listener);
    }

    /**
     * Sets an override for parameters
     * 
     * @param override
     */
    public void setParameterOverride(final Map<String, String> override) {
        this.parameterOverride = override;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#setPreference
     * (java.lang.String, java.lang.String)
     */
    @Override
    public void setPreference(final String key, final String value) {
        this.diagnosis.status("setpreference/call", new OptionInfo("key", key), new OptionInfo("value", value));
        this.sessionRecorder.setPreference(key, value);
        this.preferences.setString(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#
     * setSessionParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setSessionParameter(final String key, final String value) {
        this.diagnosis.status("setsessionparameter/call", new OptionInfo("key", key), new OptionInfo("value", value));
        this.sessionRecorder.setParameter("#sessionparameter." + key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#start()
     */
    @Override
    public void start() {
        System.out.println("BrowserPluginImpl.start()");
        if (this.diagnosis != null) this.diagnosis.status("start/call");
        if (this.recordingEnabled) this.sessionRecorder.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.applet.Applet#stop()
     */
    @Override
    public void stop() {
        System.out.println("BrowserPluginImpl.stop()");      
        this.mouseThread.interrupt();
        if (this.diagnosis != null) this.diagnosis.status("stop/call");
        if (this.recordingEnabled) this.sessionRecorder.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * testBasicFunctionality(java.lang.String)
     */
    @Deprecated
    @Override
    public void testBasicFunctionality(final String callback) {
        this.diagnosis.status("testbasicfunctionality/call", new OptionInfo("callback", callback));
        executeJSFunction(callback, "Roundtrip communication appears to be working. This means your browser successfully contacted the plugin, and the plugin was able to call the browser.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateBrowserGeometry(final int x, final int y, final int w, final int h) {
 
        final Rectangle r = new Rectangle(x, y, w, h);
        this.diagnosis.status("updatebrowsergeometry/call", new OptionInfo("rectangle", r));
        
        this.sessionRecorder.updateGeometry(r);
        this.pageManager.updateBrowserGeometry(x, y, w, h);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * updateDocumentViewport(int, int)
     */
    @Override
    public void updateDocumentViewport(final int x, final int y) {
        
        final Point p = new Point(x, y);
        this.diagnosis.status("updatedocumentviewport/call", new OptionInfo("point", p));
        
        this.sessionRecorder.updateViewport(p);
        this.pageManager.updateDocumentViewport(x, y);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#
     * updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        // We don't log this, as it will cause lots of overhead
        this.sessionRecorder.updateElementFlag(id, flag, value);
        this.pageManager.updateElementFlag(id, flag, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#
     * updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final int x, final int y,
                                      final int w, final int h) {
        // We don't log this, as it will cause lots of overhead
        this.sessionRecorder.updateElementGeometry(id, type, content, new Rectangle(x, y, w, h));
        this.pageManager.updateElementGeometry(id, type, content, x, y, w, h);
    }

    
    /** Initializes the tracking devices */
    private void initTrackingDevice() {
        this.diagnosis.status("inittrackingdevice/start");

        // Setup brain tracking device
        this.diagnosis.status("inittrackingdevice/enable/eyetracker");
        this.eyeTrackingDevice = this.deviceManager.initEyeTrackerConnection(getParameter("trackingdevice"), getParameter("trackingconnection"));
        this.eyeTrackingDevice.addTrackingListener(new EyeTrackingListener() {
            @Override
            public void newTrackingEvent(final EyeTrackingEvent event) {
                BrowserPluginImpl.this.sessionRecorder.eyeTrackingEvent(event);
            }
        });

        
        // Store the device info
        this.sessionRecorder.storeEyeDeviceInfo(this.eyeTrackingDevice.getDeviceInfo());

        // Setup eye tracking device
        if ($(getParameter("enablebraintracker")).get("false").equals("true")) {
            this.diagnosis.status("inittrackingdevice/enable/braintracker");
            this.brainTrackingDevice = this.deviceManager.initBrainTrackerConnection(null, getParameter("braintrackingconnection"));

            if (this.brainTrackingDevice != null) {
                this.diagnosis.status("inittrackingdevice/enable/braintracker/found");
                this.brainTrackingDevice.addTrackingListener(new BrainTrackingListener() {

                    @Override
                    public void newTrackingEvent(BrainTrackingEvent event) {
                        BrowserPluginImpl.this.sessionRecorder.brainTrackingEvent(event);
                    }
                });
            } else {
                this.diagnosis.status("inittrackingdevice/enable/braintracker/missing");
            }
        }

        this.diagnosis.status("inittrackingdevice/end");
    }

    /** Initializes recording of the mouse position */
    private void initMouseRecording() {
        this.diagnosis.status("initmouserecording/call");
        this.mouseThread = new Thread(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                BrowserPluginImpl.this.diagnosis.status("initmouserecording/worker/start");
                while (true) {
                    final SessionRecorder sr = BrowserPluginImpl.this.sessionRecorder;
                    final PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    final Point point = pointerInfo.getLocation();

                    if (sr != null) sr.updateMousePosition(point.x, point.y);

                    try {
                        Thread.sleep(25);
                    } catch (final InterruptedException e) {
                        return;
                    }
                }
            }
        });
        this.mouseThread.setDaemon(true);
        this.mouseThread.start();
    }

    /**
     * Gets plugin parameter specifying master file path
     */
    private void processBootstrapParameters() {
        this.recordingEnabled = Boolean.parseBoolean($(getParameter("recordingenabled")).get("true"));
        this.diagnosisEnabled = Boolean.parseBoolean($(getParameter("diagnosis")).get("true"));
        this.masterFilePath = $(getParameter("sessionpath")).get("/tmp/") + "/" + System.currentTimeMillis() + "/";
        this.updatecheck = $(getParameter("updatecheck")).get("true");

        // Try to create the masterpath
        try {
            AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                @Override
                @SuppressWarnings("boxing")
                public Boolean run() {
                    return new File(BrowserPluginImpl.this.masterFilePath).mkdirs();
                }
            });
        } catch (final Exception e) {
            System.err.println("Applet security permissions denied creating session directory. You probably forgot to grant the applet some permissions.");
            e.printStackTrace();
        }
    }

    /**
     * Obtain additional parameters
     */
    private void processAdditionalParameters() {
        this.diagnosis.status("processadditional/start");

        // Initialize the transmission mode. Determines how Java calls Javascript.
        this.transmitMode = TransmitMode.valueOf($(getParameter("transmitmode")).get("DIRECT").toUpperCase());
        this.callbackPrefix = $(getParameter("callbackprefix")).get("");

        this.diagnosis.status("processadditional/param", new OptionInfo("param", "transmitmode"), new OptionInfo("value", this.transmitMode));
        this.diagnosis.status("processadditional/param", new OptionInfo("param", "callbackprefix"), new OptionInfo("value", this.callbackPrefix));
        this.diagnosis.status("processadditional/param", new OptionInfo("param", "extensions"), new OptionInfo("value", getParameter("extensions")));

        // Load extensions
        $(getParameter("extensions")).split(";").forEach(new F1<String, String>() {
            @SuppressWarnings("synthetic-access")
            @Override
            public String f(String path) {
                try {
                    final URI uri = URI.create(path.replaceAll(" ", "%20")); 
                    BrowserPluginImpl.this.diagnosis.status("processadditional/extension/path", new OptionInfo("path", path), new OptionInfo("uri", uri));
                    BrowserPluginImpl.this.pluginManager.addPluginsFrom(uri);
                } catch (Exception e) {
                    BrowserPluginImpl.this.diagnosis.status("processadditional/extension/exception", new OptionInfo("message", e.getMessage()));
                    e.printStackTrace();
                }

                return null;
            }
        });
        
        // Process additional configuration
        final Map<String, String> config = $(getParameter("configuration")).decode().split("&").hashmap();
        
        // Read fixation parameters
        int fixationDuration = Integer.parseInt(config.get("fixation[minimumDuration]"));
        int fixationRadius = Integer.parseInt(config.get("fixation[maxFixationRadius]"));
        int fixationMinEvents = Integer.parseInt(config.get("fixation[minEvents]"));
        this.infoBroker.publish(OptionFixationParametersItem.class, new OptionFixationParameters(fixationRadius, fixationDuration, fixationMinEvents));
    }

    /**
     * Publishes various broker items
     */
    private void publishBrokerItems() {
        // Publish our service items.
        this.infoBroker.publish(PageManagerItem.class, this.pageManager);
        this.infoBroker.publish(MasterGazeHandlerItem.class, this.gazeHandler);
        this.infoBroker.publish(PseudorendererItem.class, this.pseudorender);
        this.infoBroker.publish(SessionRecorderItem.class, this.sessionRecorder);
        this.infoBroker.publish(GazeEvaluatorItem.class, this.gazeEvaluator);
        this.infoBroker.publish(JavaScriptExecutorItem.class, this);
        this.infoBroker.publish(BrowserAPIItem.class, this);

        // Publish meta items
        this.infoBroker.publish(BuildNumberItem.class, this.buildNumber);
        
        // Publish config items
        this.infoBroker.publish(TransmissionModeItem.class, this.transmitMode);
        this.infoBroker.publish(SessionDirectoryItem.class, this.masterFilePath);
    }

    /**
     * Store applet paramaters
     */
    private void storeParameters() {
        for (final String[] elem : getParameterInfo()) {
            this.sessionRecorder.setParameter(elem[0], getParameter(elem[0]));
            this.diagnosis.status("parameter", new OptionInfo(elem[0], getParameter(elem[0])));
        }
    }

    /**
     * Tell JS some status ...
     * 
     * @param status
     */
    private void tellJSStatus(final String... status) {
        executeJSFunction("_augmentedTextStatusFunction", (Object[]) status);
    }

    /**
     * Try to get a connection to the webpage.
     */
    private void tryGetWindow() {
        if (this.window == null) {
            try {
                this.window = JSObject.getWindow(this);
            } catch (final Exception e) {
                //
            }
        }
        return;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#getExtensions
     * ()
     */
    @Override
    public List<String> getExtensions() {
        return this.extensionManager.getExtensions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#
     * updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(final String id, final String key,
                                             final String value) {
        this.sessionRecorder.updateElementMetaInformation(id, key, value);
        this.pageManager.updateElementMetaInformation(id, key, value);
    }

    
    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI#logString(java.lang.String)
     */
    @Deprecated
    @Override
    public void logString(String toLog) {
        this.diagnosis.status("logstring/call", new OptionInfo("message", toLog));
        System.out.println(toLog);
    }
}
