/*
 * SessionStreamer.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import com.thoughtworks.xstream.XStream;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.BrainTrackingEventContainer;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.CallFunctionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.DeInitEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ElementGeometryEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ElementMetaInformation;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ExecuteJSEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.EyeTrackingEventContainer;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.GeometryEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.GetPreferenceEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ImageEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.InitEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.MarkEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.MouseClickEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.MouseMotionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.PropertyEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.RegisterListenerEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.RemoveListenerEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ScreenSizeEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.SetPreferenceEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.UpdateElementFlagEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ViewportEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.converter.EyeTrackingEventContainerConverter;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;
import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;

/**
 * @author Andreas Buhl
 */
@Root
public class SessionStreamer implements Serializable {
    /** Serialization version */
    private static final int VERSION = 200;

    /** Hard-coded flag whether the stream should be gzipped or not. TODO: Make this 
     * configurable in the future (or even default-on) */
    private final boolean compressed = false;

    /**  */
    private static final long serialVersionUID = -3269345193816331063L;

    /** Our logger */
    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * set the alias names for the provided XStream object
     * to tweak the xml output by replacing the full qualified object names
     * with shorter equivalents
     * 
     * @param xstream
     * 
     */
    public static void setAlias(final XStream xstream) {
        xstream.alias("Record", SessionStreamer.class);
        xstream.alias("ViewPort", ViewportEvent.class);
        xstream.alias("InitEvent", InitEvent.class);
        xstream.alias("ImageEvent", ImageEvent.class);
        xstream.alias("ElementGeometryEvent", ElementGeometryEvent.class);
        xstream.alias("MarkEvent", MarkEvent.class);
        xstream.alias("TrackingEvent", EyeTrackingEvent.class);
        xstream.alias("CallFunctionEvent", CallFunctionEvent.class);
        xstream.alias("ElementMetaInformation", ElementMetaInformation.class);
        xstream.alias("ExecuteJSEvent", ExecuteJSEvent.class);
        xstream.alias("GeometryEvent", GeometryEvent.class);
        xstream.alias("GetPreferenceEvent", GetPreferenceEvent.class);
        xstream.alias("MouseClickEvent", MouseClickEvent.class);
        xstream.alias("MouseMotionEvent", MouseMotionEvent.class);
        xstream.alias("RegisterListenerEvent", RegisterListenerEvent.class);
        xstream.alias("RemoveListenerEvent", RemoveListenerEvent.class);
        xstream.alias("SetPreferenceEvent", SetPreferenceEvent.class);
        xstream.alias("EyeTrackingEventContainer", EyeTrackingEventContainer.class);
        xstream.alias("BrainTrackingEventContainer", BrainTrackingEventContainer.class);
        xstream.alias("UpdateElementFlagEvent", UpdateElementFlagEvent.class);
        xstream.alias("DisplacementRegion", DisplacementRegion.class);
        xstream.alias("ScreenSizeEvent", ScreenSizeEvent.class);
        xstream.alias("PropertyEvent", PropertyEvent.class);
    }

    public static void registerConverters(final XStream xstream) {
        xstream.registerConverter(new EyeTrackingEventContainerConverter());
    }

    /** List of occured events */
    @ElementList
    List<AbstractSessionEvent> allEvents = new ArrayList<AbstractSessionEvent>();

    /** List of displacements for this replay */
    @ElementList(required = false)
    List<DisplacementRegion> fixationDisplacementRegions = new ArrayList<DisplacementRegion>();

    /** Last known mouse position */
    Point lastKnownMousePosition = new Point();

    /** Size of the screen when recording started */
    @Element
    Dimension screenSize;

    /** Properties we received */
    @ElementMap(required = false)
    Map<String, String> sessionProperties = new HashMap<String, String>();

    /** Event queue to for things that go to the file */
    LinkedBlockingQueue<AbstractSessionEvent> eventQueue = new LinkedBlockingQueue<AbstractSessionEvent>();

    /** If set will be used to set the next event time */
    Date nextDate;

    Thread writerThread;

    /**
     * Create a new session record.
     * 
     * @param screenSize
     * @param filename
     * @param date
     */
    public SessionStreamer(final Dimension screenSize, final String filename,
                           final Date date) {
        // Create streamer
        final XStream xstream = new XStream();
        SessionStreamer.setAlias(xstream);
        
        this.screenSize = screenSize;

        // Put initial events
        nextDate(date);
        addEvent(new InitEvent(VERSION));
        nextDate(date);
        addEvent(new ScreenSizeEvent(screenSize));

        // Generate our unique session id
        nextDate(date);
        generateUniqueSessionID();

        this.logger.fine("Create our output file " + filename);
        try {
            // (Fixed Issue #16)
            final OutputStream stream = (this.compressed) ? new GZIPOutputStream(new FileOutputStream(filename + ".gz")) : new FileOutputStream(filename);
            final ObjectOutputStream output = xstream.createObjectOutputStream(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));

            // Start background file-writer thread
            this.writerThread = new Thread(new WriterThread(output));
            this.writerThread.setDaemon(true);
            this.writerThread.start();

            // Add shutdown hook that tries to close our stream (works so-so)
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        output.writeObject(new DeInitEvent());
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Closes the streamer
     */
    public void close() {
        this.writerThread.interrupt();
    }

    /**
     * @param function
     */
    public void callFunction(final String function) {
        addEvent(new CallFunctionEvent(function));
    }

    /**
     * @param function
     * @param args
     */
    public void executeJSFunction(final String function, final String[] args) {
        addEvent(new ExecuteJSEvent(function, args));
    }

    /**
     * @return the allEvents
     */
    public List<AbstractSessionEvent> getAllEvents() {
        return this.allEvents;
    }

    /**
     * 
     * @param key
     * @param deflt
     */
    public void getPreference(final String key, final String deflt) {
        addEvent(new GetPreferenceEvent(key, deflt));
    }

    /**
     * @return .
     * @see java.util.Map#get(java.lang.Object)
     */
    public Map<String, String> getProperties() {
        return this.sessionProperties;
    }

    /**
     * @return the screenSize
     */
    public Dimension getScreenSize() {
        return this.screenSize;
    }

    /**
     * @param tag
     */
    public void markLog(final String tag) {
        addEvent(new MarkEvent(tag));

    }

    /**
     * @param type
     * @param button
     */
    public void mouseClickEvent(final int type, final int button) {
        addEvent(new MouseClickEvent(this.lastKnownMousePosition.x, this.lastKnownMousePosition.y, type, button));
    }

    /**
     * @param x
     * @param y
     * @param type
     * @param button
     */
    public void mouseClickEvent(final int x, final int y, final int type, final int button) {
        addEvent(new MouseClickEvent(x, y, type, button));
    }

    /**
     * @param x
     * @param y
     * 
     */
    public void mouseMovement(final int x, final int y) {
        this.lastKnownMousePosition.x = x;
        this.lastKnownMousePosition.y = y;
        addEvent(new MouseMotionEvent(x, y));
    }

    /**
     * 
     * @param file
     */
    public void newImage(final String file) {
        addEvent(new ImageEvent(file));
    }

    /**
     * @param key
     * @param value
     * @return .S
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public String putProperty(final String key, final String value) {
        addEvent(new PropertyEvent(key, value));
        return this.sessionProperties.put(key, value);
    }

    /**
     * @param type
     * @param listener
     */
    public void registerListener(final String type, final String listener) {
        addEvent(new RegisterListenerEvent(type, listener));
    }

    /**
     * @param listener
     */
    public void removeListener(final String listener) {
        addEvent(new RemoveListenerEvent(listener));

    }

    /**
     * @param key
     * @param value
     */
    public void setPreference(final String key, final String value) {
        addEvent(new SetPreferenceEvent(key, value));
    }

    /**
     * 
     * @param trackingEvent
     */
    public void trackingEvent(final EyeTrackingEvent trackingEvent) {
        addEvent(new EyeTrackingEventContainer(trackingEvent));
    }

    /**
     * 
     * @param p
     */
    public void updateDocumentViewport(final Point p) {
        addEvent(new ViewportEvent(p));
    }

    /**
     * @param id
     * @param flag
     * @param value
     */
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        addEvent(new UpdateElementFlagEvent(id, flag, value));
    }

    /**
     * 
     * @param id
     * @param type
     * @param content
     * @param r
     */
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final Rectangle r) {

        addEvent(new ElementGeometryEvent(id, type, content, r));
    }

    /**
     * 
     * @param r
     */
    public void updateGeometry(final Rectangle r) {
        addEvent(new GeometryEvent(r));
    }

    /**
     * @param in
     * @return .
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static AbstractSessionEvent loadFromStream(final ObjectInputStream in)
                                                                                 throws IOException,
                                                                                 ClassNotFoundException {
        return (AbstractSessionEvent) in.readObject();
    }

    /**
     * Generates a unique session id.
     */
    private void generateUniqueSessionID() {
        final long currentTime = System.currentTimeMillis();
        final long random = Math.abs(new Random().nextLong());

        final String id = currentTime + "-" + random;

        putProperty("##SID", id);
    }

    /**
     * Adds an event to the replay stream. If nextDate() was set, then the time
     * specified there is used to place the event.
     *  
     * @param evt
     */
    protected synchronized void addEvent(final AbstractSessionEvent evt) {

        // Override date if we have one
        if (this.nextDate != null) {
            evt.originalEventTime = this.nextDate.getTime();
        }

        this.eventQueue.add(evt);

        this.nextDate = null;
    }

    /**
     * @return the fixationDisplacementRegions
     */
    public synchronized List<DisplacementRegion> getFixationDisplacementRegions() {
        return this.fixationDisplacementRegions;
    }

    /**
     * @param fixationDisplacementRegions the fixationDisplacementRegions to set
     */
    public synchronized void setFixationDisplacementRegions(final List<DisplacementRegion> fixationDisplacementRegions) {
        this.fixationDisplacementRegions = fixationDisplacementRegions;
    }

    /**
     * @param id
     * @param key
     * @param value
     */
    public void updateMetaInformation(final String id, final String key,
                                      final String value) {
        addEvent(new ElementMetaInformation(id, key, value));
    }

    /**
     * @param date
     */
    public void nextDate(Date date) {
        this.nextDate = date;
    }

    /**
     * @param event2
     */
    public void brainTrackingEvent(BrainTrackingEvent event2) {
        addEvent(new BrainTrackingEventContainer(event2));
    }

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final SessionStreamer ss = new SessionStreamer(new Dimension(800, 800), "/tmp/1.zs", new Date());
        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            ss.updateGeometry(new Rectangle(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt()));
        }
        Thread.sleep(3000);
    }

    /**
     * @author Vartan
     */
    private class WriterThread implements Runnable {

        protected final ObjectOutputStream output;

        WriterThread(ObjectOutputStream output) {
            this.output = output;
        }

        @Override
        public void run() {
            try {

                while (!Thread.currentThread().isInterrupted()) {

                    // Write elements of the eventQueue to the stream until it's empty
                    while (!SessionStreamer.this.eventQueue.isEmpty()) {
                        final AbstractSessionEvent next = SessionStreamer.this.eventQueue.poll();

                        if (next == null) return;

                        // Priviled writing to stream
                        AccessController.doPrivileged(new PrivilegedAction<AbstractSessionEvent>() {

                            @Override
                            public AbstractSessionEvent run() {
                                try {
                                    WriterThread.this.output.writeObject(next);
                                } catch (final IOException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        });
                    }

                    // And flush our stream when we're done
                    AccessController.doPrivileged(new PrivilegedAction<AbstractSessionEvent>() {
                        @Override
                        public AbstractSessionEvent run() {
                            try {
                                WriterThread.this.output.flush();
                            } catch (final IOException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    });

                    // Take a breath ^^
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }
                }
            } finally {
                // Close the stream when done
                if (this.output != null) {
                    try {
                        this.output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
