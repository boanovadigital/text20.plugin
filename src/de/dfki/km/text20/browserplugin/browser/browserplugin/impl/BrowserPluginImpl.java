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

import static net.jcores.CoreKeeper.$;

import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jcores.interfaces.functions.F2ReduceObjects;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.util.JSPFProperties;
import net.xeoh.plugins.informationbroker.InformationBroker;
import net.xeoh.plugins.informationbroker.standarditems.strings.StringItem;
import net.xeoh.plugins.meta.updatecheck.UpdateCheck;
import net.xeoh.plugins.remote.RemoteAPI;
import net.xeoh.plugins.remotediscovery.RemoteDiscovery;
import netscape.javascript.JSObject;
import de.dfki.km.text20.browserplugin.browser.browserplugin.BrowserAPI;
import de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor;
import de.dfki.km.text20.browserplugin.services.devicemanager.TrackingDeviceManager;
import de.dfki.km.text20.browserplugin.services.extensionmanager.ExtensionManager;
import de.dfki.km.text20.browserplugin.services.extensionmanager.SetupParameter;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandler;
import de.dfki.km.text20.browserplugin.services.mastergazehandler.MasterGazeHandlerManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManagerManager;
import de.dfki.km.text20.browserplugin.services.persistentpreferences.PersistentPreferences;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorder;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionRecorderManager;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererManager;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingListener;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingDevice;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingListener;
import de.dfki.km.text20.util.system.OS;

/**
 * Will be instantiated by the browser.
 *
 * Main entry point!
 *
 * @author Ralf Biedert
 *
 */
public class BrowserPluginImpl extends Applet implements JSExecutor, BrowserAPI {
    private enum TransmitMode {
        ASYNC, DIRECT
    }

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

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    private InformationBroker infoBroker;

    /** Manages the related webpage */
    private PageManager pageManager;

    /** Keeps a reference to the plugin manager, in order not to overload this class */
    PluginManager pluginManager;

    /** Used to store persistent prefrences. */
    private PersistentPreferences preferences;

    /** */
    private Pseudorenderer pseudorender;

    /** */
    private ExtensionManager extensionManager;

    /** Instance id if this plugin */
    final int instanceID = new Random().nextInt();

    /** */
    final BatchHandler batchHandler = new BatchHandler(this);

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** Sets up logging */
    MasterLoggingHandler loggingHandler;

    /** Master file path */
    String masterFilePath = "/tmp";

    /** If set, getParameter will use this object to return values */
    Map<String, String> parameterOverride = null;

    /** Records screenshots and gaze points */
    SessionRecorder sessionRecorder;

    /** Executor to call javascript */
    final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /** Are we a true applet inside a browser, or a we made to believe we're only an applet but are run as an application */
    boolean thisIsTheMatrix = false;

    /** If we should keep a session record */
    boolean recordingEnabled = true;

    /** How to call JavaScript */
    TransmitMode transmitMode = TransmitMode.DIRECT;

    /** Browser window */
    JSObject window;

    /** */
    public BrowserPluginImpl() {
        System.out.println("Browser.new()");
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#batch(java.lang.String)
     */
    @Override
    public void batch(final String call) {
        this.batchHandler.batch(call);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#callFunction(java.lang.String, java.lang.String)
     */
    @Override
    public Object callFunction(final String function) {
        this.logger.finer("Received callFunction('" + function + "')");

        try {
            if (this.sessionRecorder != null) {
                this.sessionRecorder.callFunction(function);
            }

            final Pattern p = Pattern.compile("(\\w*)\\(([^\\)]*)\\).");
            final Matcher matcher = p.matcher(function);

            boolean matches = matcher.matches();

            if (!matches) {
                this.logger.warning("No match found for " + function);
                return null;
            }

            final String name = matcher.group(1);
            final String args = matcher.group(2);

            // Execute the proper extension ...
            if (this.extensionManager.getExtensions().contains(name)) {
                final Object rval = this.extensionManager.executeFunction(name, args);

                // Log the result
                if (rval != null) {
                    this.logger.fine("Returning object of type " + rval.getClass() + " with toString() value of '" + rval.toString() + "'");
                } else {
                    this.logger.fine("Returning null value");
                }

                return rval;
            }

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#destroy()
     */
    @Override
    public void destroy() {
        this.pluginManager.shutdown();
        this.loggingHandler.shutdown();
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#getParameter(java.lang.String)
     */

    /**
     * Execute a script inside the browser
     *
     * (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.browser.browserplugin.JSExecutor#executeJSFunction(java.lang.String, java.lang.Object[])
     */
    @Override
    public Object executeJSFunction(final String _function, final Object... args) {
        // In case we're in the matrix, dont try to call applet function, otherwise Agent Smith will
        // roundhouse kick your butt.
        if (this.thisIsTheMatrix) { return null; }

        // Append the callback prefix to the function.
        final String function = this.callbackPrefix + _function;

        //if (true) this.logger.info("Calling function + '" + function + "'");

        tryGetWindow();

        this.sessionRecorder.executeJSFunction(function, args);

        // If the window is still null, we try our fallback solution (maybe slow ...)
        if (this.window == null) {
            this.logger.warning("Unable to execute JS function : " + function + ". Did you forget to specifiy mayscript='yes' in the applet-tag?");

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

            this.logger.warning("Trying dirty fallback, calling : " + sb.toString());

            try {
                final AppletContext appletContext = getAppletContext();
                appletContext.showDocument(new URL(sb.toString()));
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }

        // This is the ugly way (Safari likes it)
        if (this.transmitMode.equals(TransmitMode.ASYNC)) {
            this.singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        BrowserPluginImpl.this.window.call(function, args);
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return null;
        }

        // This is the nice way (Firefox likes it)
        if (this.transmitMode.equals(TransmitMode.DIRECT)) {
            try {
                return this.window.call(function, args);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String key) {
        if (this.parameterOverride == null) { return super.getParameter(key); }

        return this.parameterOverride.get(key);
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#getParameterInfo()
     */
    @Override
    public String[][] getParameterInfo() {
        return new String[][] { { "transmitmode", "string", "What to use to call JavaScript" }, { "trackingdevice", "string", "Identifies the device handler" }, { "trackingconnection", "url", "If it is a remote device, where to contact?" }, { "sessionpath", "string", "Save all stuff to what?" }, { "callbackprefix", "string", "Appended to all callbacks from the applet via liveconnect." } };
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#getPreference(java.lang.String, java.lang.String)
     */
    @Override
    public String getPreference(final String key, final String deflt) {
        this.sessionRecorder.getPreference(key, deflt);
        final String rval = this.preferences.getString(key, deflt);
        this.logger.finer("Received getPreference('" + key + "', '" + deflt + "') = " + rval);
        return rval;
    }

    /**
     * Wird seltener aufgerufen als start, sollte hier Verbindung mit dem Eye-Tracker-
     * Server aufnehmen und das Pluginframework initialisieren.
     */
    /* (non-Javadoc)
     * @see java.applet.Applet#init()
     */
    @Override
    public void init() {
        System.out.println("Plugin.init()");

        // We want to save from the first second
        processBootstrapParameters();

        final JSPFProperties props = new JSPFProperties();
        props.setProperty(RemoteAPI.class, "proxy.timeout", "1000");
        props.setProperty(RemoteDiscovery.class, "startup.locktime", "1000");
        props.setProperty(UpdateCheck.class, "update.url", "http://api.text20.net/common/versioncheck/");
        props.setProperty(UpdateCheck.class, "update.enabled", this.updatecheck);
        props.setProperty(UpdateCheck.class, "product.name", "text20.plugin");
        props.setProperty(UpdateCheck.class, "product.version", "1.3"); // TODO: Get this version number from a better place!

        setupEarlyLogging(props);

        this.logger.info("ID of this instance " + this.instanceID + " init()");

        this.pluginManager = new FrameworkManager(props).getPluginManager();

        this.extensionManager = this.pluginManager.getPlugin(ExtensionManager.class);
        this.sessionRecorder = this.pluginManager.getPlugin(SessionRecorderManager.class).createSessionRecorder();
        this.deviceManager = this.pluginManager.getPlugin(TrackingDeviceManager.class);
        this.infoBroker = this.pluginManager.getPlugin(InformationBroker.class);
        this.preferences = this.pluginManager.getPlugin(PersistentPreferences.class);
        this.pseudorender = this.pluginManager.getPlugin(PseudorendererManager.class).createPseudorenderer();
        this.gazeHandler = this.pluginManager.getPlugin(MasterGazeHandlerManager.class).createMasterGazeHandler(this, this.pseudorender);
        this.pageManager = this.pluginManager.getPlugin(PageManagerManager.class).createPageManager(this.pseudorender);

        // Store parameters
        storeParameters();

        // Evaluate additional parameter
        processAdditionalParameters();

        // setup extensions
        setupExtensions();

        // Setup gaze recording
        initTrackingDevice();

        // Setup mouse recording
        initMouseRecording();

        // Publish items of the information broker
        publishBrokerItems();

        // Try to get the window
        tryGetWindow();

        // Provide the tracking device to other listeners.
        this.gazeHandler.setTrackingDevice(this.deviceManager.getEyeTrackingDevice());

        tellJSStatus("INITIALIZED");
    }

    /**
     * @param props
     */
    private void setupEarlyLogging(final JSPFProperties props) {

        Level level = null;

        // Setup JSPF logging level
        final String logging = $(getParameter("logging")).get("default");

        if (!logging.equals("default")) {
            level = Level.parse(logging);
        }

        // Initialize logging handler and others ...
        try {
            this.loggingHandler = new MasterLoggingHandler(this.masterFilePath, level);
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Setup all known extensions
     */
    private void setupExtensions() {
        for (final SetupParameter parameter : SetupParameter.values()) {

            Object value = null;

            switch (parameter) {
            case SESSION_RECORDER:
                value = this.sessionRecorder;
                break;

            case BROWSER_API:
                value = this;
                break;

            case GAZE_HANDLER:
                value = this.gazeHandler;
                break;

            case JAVASCRIPT_EXECUTOR:
                value = this;
                break;

            case PSEUDO_RENDERER:
                value = this.pseudorender;
                break;

            default:
                this.logger.warning("Warning. Parameter " + parameter + " not passed to extensions!");
            }

            // Set the given parameter
            this.extensionManager.setParameter(parameter, value);
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#logString(java.lang.String)
     */
    @Override
    public void logString(final String toLog) {
        this.logger.info(toLog);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#registerListener(java.lang.String, java.lang.String)
     */
    @Override
    public void registerListener(final String type, final String listener) {
        this.logger.fine("Registering listener of type " + type + " with name " + listener);
        this.sessionRecorder.registerListener(type, listener);
        this.gazeHandler.registerJSCallback(type, listener);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#removeListener(java.lang.String)
     */
    @Override
    public void removeListener(final String listener) {
        this.sessionRecorder.removeListener(listener);
        this.gazeHandler.removeJSCallback(listener);
    }

    /**
     * Call this if you're in applet mode
     *
     * @param value
     */
    public void setBrowserImitation(final boolean value) {
        this.thisIsTheMatrix = value;
    }

    /**
     * Sets an override for parameters
     *
     * @param override
     */
    public void setParameterOverride(final Map<String, String> override) {
        this.parameterOverride = override;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#setPreference(java.lang.String, java.lang.String)
     */
    @Override
    public void setPreference(final String key, final String value) {
        this.logger.finer("Received setPreference('" + key + "', '" + value + "')");
        this.sessionRecorder.setPreference(key, value);
        this.preferences.setString(key, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#setSessionParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setSessionParameter(final String key, final String value) {
        this.sessionRecorder.setParameter("#sessionparameter." + key, value);
    }

    /**
     * @param one
     * @param two
     * @param three
     * @param four
     */
    public void simpleBenchmark(final String one, final String two, final String three,
                                final String four) {
        this.logger.info("new couple: " + one + " " + two + " " + three + " " + four);
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#start()
     */
    @Override
    public void start() {
        if (this.recordingEnabled) {
            this.sessionRecorder.start();
        }
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#stop()
     */
    @Override
    public void stop() {
        if (this.recordingEnabled) {
            this.sessionRecorder.stop();
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#testBasicFunctionality(java.lang.String)
     */
    @Override
    public void testBasicFunctionality(final String callback) {
        this.logger.info("testBasicFunctionality() reached!");

        executeJSFunction(callback, "Roundtrip communication appears to be working. This means your browser successfully contacted the plugin, and the plugin was able to call the browser.");
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateBrowserGeometry(final int x, final int y, final int w, final int h) {
        this.sessionRecorder.updateGeometry(new Rectangle(x, y, w, h));
        this.pageManager.updateBrowserGeometry(x, y, w, h);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#updateDocumentViewport(int, int)
     */
    @Override
    public void updateDocumentViewport(final int x, final int y) {
        this.sessionRecorder.updateViewport(new Point(x, y));
        this.pageManager.updateDocumentViewport(x, y);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        this.sessionRecorder.updateElementFlag(id, flag, value);
        this.pageManager.updateElementFlag(id, flag, value);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.impl.BrowserAPI#updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final int x, final int y,
                                      final int w, final int h) {
        this.sessionRecorder.updateElementGeometry(id, type, content, new Rectangle(x, y, w, h));
        this.pageManager.updateElementGeometry(id, type, content, x, y, w, h);
    }

    /**
     * Initializes the tracking devices
     */
    private void initTrackingDevice() {

        // Setup brain tracking device
        this.deviceManager.initEyeTrackerConnection(getParameter("trackingdevice"), getParameter("trackingconnection"));
        this.deviceManager.getEyeTrackingDevice().addTrackingListener(new EyeTrackingListener() {

            @Override
            public void newTrackingEvent(final EyeTrackingEvent event) {
                BrowserPluginImpl.this.sessionRecorder.newTrackingEvent(event);
            }
        });

        // Store the device info
        final EyeTrackingDevice trackingDevice = this.deviceManager.getEyeTrackingDevice();
        this.sessionRecorder.storeDeviceInfo(trackingDevice.getDeviceInfo());

        // Setup eye tracking device
        if ($(getParameter("enablebraintracker")).get("false").equals("true")) {
            this.logger.info("Enabling Brain Tracker");
            this.deviceManager.initBrainTrackerConnection(null, getParameter("braintrackingconnection"));

            final BrainTrackingDevice device = this.deviceManager.getBrainTrackingDevice();

            if (device != null) {
                device.addTrackingListener(new BrainTrackingListener() {

                    @Override
                    public void newTrackingEvent(BrainTrackingEvent event) {
                        BrowserPluginImpl.this.sessionRecorder.newBrainTrackingEvent(event);
                    }
                });
            }
        }
    }

    /**
     * Initializes recording of the mouse position
     */
    private void initMouseRecording() {
        // Start a background thread to record the current mouse position.
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final SessionRecorder sr = BrowserPluginImpl.this.sessionRecorder;
                    final PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    final Point point = pointerInfo.getLocation();

                    if (sr != null) sr.updateMousePosition(point.x, point.y);

                    try {
                        Thread.sleep(25);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Gets plugin parameter specifying master file path
     */
    private void processBootstrapParameters() {
        this.recordingEnabled = Boolean.parseBoolean($(getParameter("recordingenabled")).get("true"));
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
            this.logger.warning("Applet security permissions denied creating session directory. You probably forgot to grant the applet some permissions.");
            e.printStackTrace();
        }

    }

    /**
     * Obtain additional parameters
     */
    private void processAdditionalParameters() {
        // Initialize the transmission mode. Determines how Java calls Javascript.
        this.transmitMode = TransmitMode.valueOf($(getParameter("transmitmode")).get("DIRECT").toUpperCase());
        this.callbackPrefix = $(getParameter("callbackprefix")).get("");

        $(getParameter("extensions")).split(";").reduce(new F2ReduceObjects<String>() {
            @Override
            public String f(String left, String path) {
                try {
                    final URI uri = OS.absoluteBrowserPathToURI(path);
                    BrowserPluginImpl.this.logger.info("Trying to load user defined extension at " + uri);
                    BrowserPluginImpl.this.pluginManager.addPluginsFrom(uri);
                } catch (Exception e) {
                    BrowserPluginImpl.this.logger.warning("Unable to load extension " + path);
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    /**
     *
     */
    private void publishBrokerItems() {
        // Publish information broker items
        this.infoBroker.publish(new StringItem("global:transmitMode", this.transmitMode.toString()));
        this.infoBroker.publish(new StringItem("global:sessionDir", this.masterFilePath));
    }

    /**
     * Store applet paramaters
     */
    private void storeParameters() {
        for (final String[] elem : getParameterInfo()) {
            this.sessionRecorder.setParameter(elem[0], getParameter(elem[0]));
        }
    }

    /**
     * Tell JS some status ...
     *
     * @param status
     */
    private void tellJSStatus(final String status) {
        executeJSFunction("_augmentedTextStatusFunction", status);
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

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#getExtensions()
     */
    @Override
    public List<String> getExtensions() {
        return this.extensionManager.getExtensions();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.browser.browserplugin.BrowserAPI#updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(final String id, final String key,
                                             final String value) {
        this.sessionRecorder.updateElementMetaInformation(id, key, value);
        this.pageManager.updateElementMetaInformation(id, key, value);
    }
}
