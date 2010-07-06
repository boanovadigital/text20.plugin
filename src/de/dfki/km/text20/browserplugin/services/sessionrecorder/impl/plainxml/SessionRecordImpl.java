/*
 * SessionRecord.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.CallFunctionEvent;
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
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.RegisterListenerEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.RemoveListenerEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.SetPreferenceEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.UpdateElementFlagEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ViewportEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.util.system.IO;

/**
 * 
 * @author rb
 *
 */
@Root
public class SessionRecordImpl implements Serializable {

    /** */
    private static final long serialVersionUID = -4845405572472719648L;

    /**
     * @param file
     * @return .
     */
    public static SessionRecordImpl loadFrom(final String file) {
        return loadFrom(file, true);
    }

    /**
     * @param file
     * @param tryMerge 
     * @return .
     */
    public static SessionRecordImpl loadFrom(final String file, final boolean tryMerge) {

        final File given = new File(file);
        final File path = given.getParentFile();

        // First, check if there already is a merged file. In that case, load it.
        if (new File(file + ".merged").exists()) {
            try {
                final Serializer serializer = new Persister(new XMLTransformationMatcher());
                return serializer.read(SessionRecordImpl.class, new File(file + ".merged"));
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        // Check how many files we have
        final File[] listFiles = path.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                if (name.endsWith("session.xml")) return false;
                if (name.endsWith(".xml")) return true;
                return false;
            }
        });

        String suffix = "";

        // TODO: Check last .xml if it's corrupt or not.

        // If we have more files, merge them
        if (listFiles.length > 1 && tryMerge) {
            /*
            final String[] files = new String[listFiles.length];

            for (int i = 0; i < files.length; i++) {
                files[i] = SessionMerger.readFileAsString(listFiles[i].getAbsolutePath());
            }

            final String merge = SessionMerger.merge(files);

            suffix = ".merged";

            try {
                final BufferedWriter out = new BufferedWriter(new FileWriter(file + suffix));
                out.write(merge);
                out.close();
            } catch (final IOException e1) {
                e1.printStackTrace();
            }*/
        }

        final File inFile = new File(file + suffix);
        File tmp = null;
        try {
            tmp = File.createTempFile("prefix", "suffix");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        final List<String> readLines = IO.readLines(inFile);
        final List<String> writeLines = new ArrayList<String>();
        for (String string : readLines) {
            String replaced = string;

            replaced = replaced.replaceAll("de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.events", "de.dfki.km.text20.browserplugin.services.sessionrecorder.events");
            replaced = replaced.replaceAll("de.dfki.km.augmentedtext.util.recorder.events", "de.dfki.km.text20.browserplugin.services.sessionrecorder.events");

            //System.out.println(replaced);
            writeLines.add(replaced);

        }
        IO.writeLines(tmp, writeLines);

        final SessionRecordImpl rval = null;
        try {
            final Serializer serializer = new Persister(new XMLTransformationMatcher());
            return serializer.read(SessionRecordImpl.class, tmp);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return rval;
    }

    /** If set it will be used to override the date for the next event. */
    private transient Date overrideDate;

    /** List of occured events */
    @ElementList
    List<AbstractSessionEvent> allEvents = new ArrayList<AbstractSessionEvent>();

    @ElementList(required = false)
    List<DisplacementRegion> fixationDisplacementRegions = new ArrayList<DisplacementRegion>();

    /** */
    Point lastKnownMousePosition = new Point();

    /** Size of the screen when recording started */
    @Element
    Dimension screenSize;

    @ElementMap(required = false)
    Map<String, String> sessionProperties = new HashMap<String, String>();

    /**
     * Create a new session record.
     * 
     * @param screenSize
     */
    public SessionRecordImpl(final Dimension screenSize) {
        this.screenSize = screenSize;
        addEvent(new InitEvent());

        generateUniqueSessionID();
    }

    /**
     * Used for serialization
     */
    protected SessionRecordImpl() {
        //
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
        return new ArrayList<AbstractSessionEvent>(this.allEvents);
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
     * Can be used to override the date for the next event
     * @param date
     */
    public void overrideDateForNextEvent(final Date date) {
        this.overrideDate = date;
    }

    /**
     * @param key
     * @param value
     * @return .S
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public String putProperty(final String key, final String value) {
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
     * @param file
     */
    public synchronized void writeTo(final String file) {
        writeTo(file, false);
    }

    /**
     * Writes this object to a file
     * 
     * @param file
     * @param incremental 
     */
    public synchronized void writeTo(final String file, final boolean incremental) {
        if (incremental) {
            try {
                final long time = System.currentTimeMillis();
                final String clusterFile = file.replaceAll("\\.xml", "." + time + ".xml");
                final Serializer serializer = new Persister(new XMLTransformationMatcher());
                serializer.write(this, new File(clusterFile));
                this.allEvents.clear();

                // Touch reference file
                new File(file).createNewFile();
            } catch (final Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                final Serializer serializer = new Persister(new XMLTransformationMatcher());
                serializer.write(this, new File(file));
            } catch (final Exception e) {
                e.printStackTrace();
            }

        }
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
     * 
     * @param event
     */
    protected synchronized void addEvent(final AbstractSessionEvent event) {

        if (this.overrideDate != null) {
            event.originalEventTime = this.overrideDate.getTime();
            this.overrideDate = null;
        }

        this.allEvents.add(event);
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
    public synchronized void setFixationDisplacementRegions(
                                                            final List<DisplacementRegion> fixationDisplacementRegions) {
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

}
