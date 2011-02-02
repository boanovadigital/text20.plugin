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
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import net.xeoh.plugins.base.util.OptionUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

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
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionWaitForFinish;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * Current replay implementation
 * 
 * @author Ralf Biedert
 * @author Arman Vartan
 */
public class SessionReplayImpl implements SessionReplay {

    /**
     * The file to replay. This can either be a zip file (with an internal .xstream) or an
     * xstream directly.
     */
    final File file;

    /** Logger */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /** Xstream (de)serializer */
    final XStream xstream = new XStream();

    /** List of events to filter */
    final List<Class<? extends AbstractSessionEvent>> toFilter = new ArrayList<Class<? extends AbstractSessionEvent>>();

    /** List of properties stored in the replay */
    final Map<String, String> propertyMap = new HashMap<String, String>();

    /** Input stream to read the content from */
    ObjectInputStream in;
    
    /** The recorded screen size */
    Dimension screenSize;

    /** The loader to access elements */
    AbstractLoader loader = null;

    /** Displacement regions to apply */
    List<DisplacementRegion> fixationDisplacementRegions;

    
    /**
     * Creates a new replay implementation for the given file
     * 
     * @param file
     */
    public SessionReplayImpl(final File file) {
        if (!file.exists()) { throw new IllegalArgumentException("file: " + file.getAbsolutePath() + " does not exist"); }

        this.file = file;

        SessionStreamer.setAlias(this.xstream);
        SessionStreamer.registerConverters(this.xstream);

        // Parse file to get properties and screen size
        this.getMetaInfo();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getDisplacements()
     */
    @Override
    public List<DisplacementRegion> getDisplacements() {
        return new ArrayList<DisplacementRegion>();
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getProperties(java.lang.String[])
     */
    @Override
    public Map<String, String> getProperties(String... properties) {
        return this.propertyMap;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getScreenSize()
     */
    @Override
    public Dimension getScreenSize() {
        return this.screenSize;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#replay(de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener, de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption[])
     */
    @Override
    public synchronized void replay(final ReplayListener listener,
                                    final ReplayOption... options) {
        createInputStream();

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
        final AtomicBoolean waitAfterFinish = new AtomicBoolean(false);
        final CyclicBarrier finishBarrier = new CyclicBarrier(2);
        // final AtomicLong realtimeDuration = new AtomicLong();

        // Process options
        final OptionUtils<ReplayOption> ou = new OptionUtils<ReplayOption>(options);
        if (ou.contains(OptionWaitForFinish.class)) waitAfterFinish.set(true);
        if (ou.contains(OptionGetMetaInfo.class)) gettingMetaInfo.set(true);
        if (ou.contains(OptionRealtime.class)) realtimeReplay.set(true);
        if (ou.contains(OptionLoadImages.class)) loadImages.set(true);
        if (ou.contains(OptionSlowMotion.class)) {
            realtimeReplay.set(true);
            slowdownFactor.set(ou.get(OptionSlowMotion.class).getFactor());
        }

        // (Fixed Issue #30)
        // Create the actual replay thread
        final Thread t = new Thread(new Runnable() {

            private boolean hasMore = true;

            @Override
            public void run() {

                try {
                    AbstractSessionEvent previousEvent = null;

                    // As long as we have more events
                    while (this.hasMore) {
                        AbstractSessionEvent event = null;

                        try {
                            // Load the next event
                            event = (AbstractSessionEvent) SessionReplayImpl.this.in.readObject();

                            // In case we have no previous event, save the first event
                            // time
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

                            // Check what kind of event it is and if we have some special
                            // rules
                            if (loadImages.get() && event instanceof ImageEvent) {
                                final ImageEvent e = (ImageEvent) event;

                                // TODO: Image loading should be fixed... it's still using
                                // a special loader only used with zip files...
                                if (SessionReplayImpl.this.loader == null) {
                                    continue;
                                }

                                final InputStream is = SessionReplayImpl.this.loader.getFile(e.associatedFilename);
                                final BufferedImage read = ImageIO.read(is);

                                event = new PseudoImageEvent(e, read);
                            }

                            // Now we are permitted to fire the event.
                            try {
                                listener.nextEvent(event);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            previousEvent = event;
                        } catch(StreamException e) { 
                            this.hasMore = false;
                        } catch (EOFException e) {
                            this.hasMore = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    try {
                        // Check if we should signal that we have finished
                        if (waitAfterFinish.get()) {
                            finishBarrier.await();
                        }

                        // Close the input stream
                        SessionReplayImpl.this.in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        t.setDaemon(true);
        t.start();

        // Java can be so fcking ugly.
        if (waitAfterFinish.get()) {
            try {
                finishBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    /** Tries to open the input stream depending on the method */
    private void createInputStream() {
        try {
            InputStream input = null;

            // First check if we have a .zip ...
            if (this.file.getAbsolutePath().endsWith(".zip")) {
                this.loader = new ZIPLoader(this.file);
                input = this.loader.getSessionInputStream();
            }

            // ... and check if we have .xstream file
            if (this.file.getAbsolutePath().endsWith(".xstream")) {
                input = new FileInputStream(this.file);
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
    }

    /** Gets meta information from the replay stream */
    private void getMetaInfo() {
        this.createInputStream();

        boolean isFinishedReading = false;

        // We open the file and scan for certain events ...
        while (!isFinishedReading) {
            try {
                final AbstractSessionEvent event = (AbstractSessionEvent) this.in.readObject();

                // Screen size so we know how large the original desktop was
                if (event instanceof ScreenSizeEvent) {
                    this.screenSize = ((ScreenSizeEvent) event).screenSize;
                    continue;
                }

                // Property events might also be required a priori
                if (event instanceof PropertyEvent) {
                    this.propertyMap.put(((PropertyEvent) event).key, ((PropertyEvent) event).value);
                    continue;
                }

            } catch(StreamException e) { 
                isFinishedReading = true;
            } catch (EOFException e) {
                isFinishedReading = true;
                // TODO: Is this neccessary and if yes, how to implement it correctly?
                // realtimeDuration.set(currentEvenTime.get() - firstEventTime.get());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // Eventually close the stream 
        try {
            this.in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return .
     */
    public synchronized List<DisplacementRegion> getFixationDisplacementRegions() {
        return this.fixationDisplacementRegions;
    }

    /**
     * @param fixationDisplacementRegions
     */
    public synchronized void setFixationDisplacementRegions(final List<DisplacementRegion> fixationDisplacementRegions) {
        this.fixationDisplacementRegions = fixationDisplacementRegions;
    }

    /**
     * Events of that class wont be passed.
     * 
     * @param filter
     */
    @SuppressWarnings("unused")
    private void addFilter(final Class<? extends AbstractSessionEvent> filter) {
        this.toFilter.add(filter);
    }
}
