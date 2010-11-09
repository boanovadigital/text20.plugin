/*
 * SessionReplayImpl.java
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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import net.xeoh.plugins.base.util.OptionUtils;

import com.thoughtworks.xstream.XStream;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ImageEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.PropertyEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ScreenSizeEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.pseudo.PseudoImageEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.AbstractLoader;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.ZIPLoader;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionGetMetaInfo;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionLoadImages;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionRealtime;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionSlowMotion;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * @author rb
 *
 */
public class SessionReplayImpl implements SessionReplay {

    /** Logger */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** Xstream (de)serializer */
    final XStream xstream = new XStream();

    /** Makes other threads wait for this element to be finished. */
    final Lock finishedLock = new ReentrantLock();

    /** Input stream to read the content from */
    ObjectInputStream in;

    /** List of events to filter */
    final List<Class<? extends AbstractSessionEvent>> toFilter = new ArrayList<Class<? extends AbstractSessionEvent>>();

    /** List of properties stored in the replay */
    final Map<String, String> propertyMap = new HashMap<String, String>();

    /** The recorded screen size */
    Dimension screenSize;

    /** The file to replay. This can either be a zip file (with an internal .xstream) or an xstream directly. */
    private File file;

    /** The loader to access elements */
    AbstractLoader loader = null;

    /** Displacement regions to apply */
    private List<DisplacementRegion> fixationDisplacementRegions;

    /**
     * @param file
     */
    public SessionReplayImpl(final File file) {
        if (!file.exists())
            throw new IllegalArgumentException("file: " + file.getAbsolutePath() + " does not exist ");

        this.file = file;

        SessionStreamer.setAlias(this.xstream);
        SessionStreamer.registerConverters(this.xstream);

        // Parse file to get properties and screen size
        // TODO: Why do we always get the MetaInfo?
        replay(null, new OptionGetMetaInfo());

        waitForFinish();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getDisplacements()
     */
    @Override
    public List<DisplacementRegion> getDisplacements() {
        return new ArrayList<DisplacementRegion>();
    }

    /**
     * @return .
     */
    public synchronized List<DisplacementRegion> getFixationDisplacementRegions() {
        return this.fixationDisplacementRegions;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getProperties()
     */
    @Override
    public Map<String, String> getProperties(String... properties) {
//        waitForFinish();
        return this.propertyMap;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getScreenSize()
     */
    @Override
    public Dimension getScreenSize() {
//        waitForFinish();
        return this.screenSize;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#replay(de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.ReplayListener, de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.options.ReplayOption[])
     */
    @Override
    public synchronized void replay(final ReplayListener listener, final ReplayOption... options) {

        try {
            InputStream input = null;

            // First check if we have a .zip ...
            if (this.file.getAbsolutePath().endsWith(".xstream")) {
                input = new FileInputStream(this.file);
            }

            // ... and check if we have .xstream file
            if (this.file.getAbsolutePath().endsWith(".zip")) {
                this.loader = new ZIPLoader(this.file);
                input = this.loader.getSessionInputStream();
            }

            // ... and it might be a gz file
            // (Fixed Issue #16)
            if (this.file.getAbsolutePath().endsWith(".gz")) {
                input = new GZIPInputStream(new FileInputStream(this.file));
            }

            // (Fixed Issue #26)
            this.in = this.xstream.createObjectInputStream(new BufferedReader(new InputStreamReader(input, "UTF-8")));

        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Sanity check
        if (this.in == null) {
            this.logger.warning("Unable to load replay for file " + this.file);
            return;
        }

        // Options
        final AtomicBoolean gettingMetaInfo = new AtomicBoolean(false);
        final AtomicBoolean realtimeReplay = new AtomicBoolean(false);
        final AtomicBoolean loadImages = new AtomicBoolean(false);

        // Some variables
        final AtomicLong slowdownFactor = new AtomicLong(1);
        final AtomicLong currentEvenTime = new AtomicLong();
        final AtomicLong firstEventTime = new AtomicLong();
        final AtomicLong realtimeDuration = new AtomicLong();

        // Process options
        final OptionUtils<ReplayOption> ou = new OptionUtils<ReplayOption>(options);
        if (ou.contains(OptionGetMetaInfo.class)) gettingMetaInfo.set(true);
        if (ou.contains(OptionRealtime.class)) realtimeReplay.set(true);
        if (ou.contains(OptionLoadImages.class)) loadImages.set(true);
        if (ou.contains(OptionSlowMotion.class)) {
            realtimeReplay.set(true);
            slowdownFactor.set(ou.get(OptionSlowMotion.class).getFactor());
        }

        // Create a barrier that allows us to wait for synchronous replay
        // TODO: Remove barriers... Options: 1) Singlethread + waiting 2) Multithreaded multicallable
        // (Issue #30)
        final CyclicBarrier barrier = new CyclicBarrier(2);

        // Create the actual replay thread
        final Thread thread = new Thread(new Runnable() {

            private boolean hasMore = true;

            @Override
            public void run() {
                try {
                    // Lock until we finished
                    SessionReplayImpl.this.finishedLock.lock();

                    // Synchronize with exit of the function
                    try {
                        barrier.await();
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    } catch (final BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    AbstractSessionEvent previousEvent = null;

                    // As long as we have more events
                    while (this.hasMore) {
                        AbstractSessionEvent event = null;

                        try {
                            // Load the next event
                            event = (AbstractSessionEvent) SessionReplayImpl.this.in.readObject();

                            // In case we have no previous event, save the first event time
                            if (previousEvent == null) {
                                firstEventTime.set(event.originalEventTime);
                                previousEvent = event;
                            }

                            // Store current event time
                            currentEvenTime.set(event.originalEventTime);

                            // Dont process if filtered
                            if (SessionReplayImpl.this.toFilter.contains(event.getClass())) {
                                continue;
                            }

                            // Check if we only get meta events ...
                            if (gettingMetaInfo.get()) {
                                if (event instanceof ScreenSizeEvent) {
                                    SessionReplayImpl.this.screenSize = ((ScreenSizeEvent) event).screenSize;
                                }

                                if (event instanceof PropertyEvent) {
                                    SessionReplayImpl.this.propertyMap.put(((PropertyEvent) event).key, ((PropertyEvent) event).value);
                                }

                                continue;
                            }

                            // Can be switched off, to make replay as fast as possible.
                            if (realtimeReplay.get()) {
                                long delta = event.originalEventTime - previousEvent.originalEventTime;

                                // TODO: When does this happen?
                                if (delta < 0) {
                                    SessionReplayImpl.this.logger.fine("Event times are mixed up " + event.originalEventTime + " < " + previousEvent.originalEventTime);
                                    delta = 0;
                                }

                                // And now wait for the given time
                                try {
                                    Thread.sleep(delta * slowdownFactor.get());
                                } catch (final InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            // Check what kind of event it is and if we have some special rules
                            if (event instanceof ImageEvent && loadImages.get()) {
                                final ImageEvent e = (ImageEvent) event;
                                final InputStream is = SessionReplayImpl.this.loader.getFile(e.associatedFilename);
                                final BufferedImage read = ImageIO.read(is);

                                event = new PseudoImageEvent(e, read);
                            }

                            // Now we are permitted to fire the event.
                            if (listener != null) {
                                try {
                                    listener.nextEvent(event);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            previousEvent = event;
                        } catch (EOFException e) {
//                            SessionReplayImpl.this.logger.info("EOFException thrown. File might not be closed correctly when it was written.");

                            this.hasMore = false;
                            if (gettingMetaInfo.get()) {
                                realtimeDuration.set(currentEvenTime.get() - firstEventTime.get());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    SessionReplayImpl.this.finishedLock.unlock();

                    try {
                        // TODO: Why is the barrier awaited again, because it has a size of 2 and is already
                        // awaited by the surrounding method's end and this thread's start.
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        // Synchronize with starting of thread.
        try {
            barrier.await();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        } catch (final BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fixationDisplacementRegions
     */
    public synchronized void setFixationDisplacementRegions(final List<DisplacementRegion> fixationDisplacementRegions) {
        this.fixationDisplacementRegions = fixationDisplacementRegions;
    }

    /**
     * Waits until the thread has finished.
     */
    public void waitForFinish() {
        // If the lock is unlocked, do nothing
        if (this.finishedLock.tryLock()) return;

        // If it's locked, wait.
        this.finishedLock.lock();
        this.finishedLock.unlock();

        return;
    }

//    /**
//     * Events of that class wont be passed.
//     *
//     * @param filter
//     */
//    private void addFilter(final Class<? extends AbstractSessionEvent> filter) {
//        this.toFilter.add(filter);
//    }
}
