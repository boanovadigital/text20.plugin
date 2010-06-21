package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream;

import java.awt.Dimension;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.xeoh.plugins.base.util.OptionUtils;

import com.thoughtworks.xstream.XStream;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.ReplayListener;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.PropertyEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.ScreenSizeEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.ReplayOption;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionGetMetaInfo;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionRealtime;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.options.replay.OptionSlowMotion;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.util.metadata.DisplacementRegion;

/**
 * @author rb
 *
 */
public class SessionReplayImpl implements SessionReplay {

    /**
     * Makes other threads wait for this element to be finished. 
     */
    Lock finishedLock = new ReentrantLock();

    /** Should we print timing information */
    @SuppressWarnings("unused")
    private final boolean printTiming = true;

    /** If the replay is in realtime, or as fast as possible */
    boolean realtimeReplay = false;

    /** controls the behaviour of the replay method 
     * if true, only the property and screen size 'events' are fetched and stored
     * if false, the listeners will work   
     * */
    private boolean gettingMetaInfo = false;

    /** How far to slow down "realtime" */
    int slowdownFactor = 1;

    /** List of events to filter */
    final List<Class<? extends AbstractSessionEvent>> toFilter = new ArrayList<Class<? extends AbstractSessionEvent>>();

    /** */
    ObjectInputStream in;

    /** */
    Map<String, String> propertyMap = new HashMap<String, String>();

    /** the recorded screen size */
    Dimension screenSize;

    /** */
    final XStream xstream = new XStream();

    /** the file to replay */
    private File file;

    /** */
    protected long firstEventtime;

    /** */
    protected long currentEventime;

    /** */
    protected long realtimeDuration;

    /** */
    private List<DisplacementRegion> fixationDisplacementRegions;

    /**
     * @param file 
     */
    public SessionReplayImpl(final File file) {
        if (!file.exists())
            throw new IllegalArgumentException("file: " + file.getAbsolutePath() + " does not exist ");

        this.file = file;

        SessionStreamer.setAlias(this.xstream);

        // parse file to get properties and screen size
        replay(null, new OptionGetMetaInfo());
        waitForFinish();
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

    /**
     * @return the realtimeReplay
     */
    boolean isRealtimeReplay() {
        return this.realtimeReplay;
    }

    /**
     * @param realtimeReplay the realtimeReplay to set
     */
    public void setRealtimeReplay(final boolean realtimeReplay) {
        this.realtimeReplay = realtimeReplay;
    }

    /**
     * @param factor
     */
    public void setSlowdown(final int factor) {
        this.slowdownFactor = factor;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#replay(de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.ReplayListener, de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.options.ReplayOption[])
     */
    public synchronized void replay(final ReplayListener listener,
                                    final ReplayOption... options) {

        final CyclicBarrier barrier = new CyclicBarrier(2);

        this.finishedLock = new ReentrantLock();
        try {
            this.in = this.xstream.createObjectInputStream(new FileReader(this.file));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        setGettingMetaInfo(false);
        setRealtimeReplay(false);

        // Process options
        final OptionUtils<ReplayOption> ou = new OptionUtils<ReplayOption>(options);
        if (ou.contains(OptionGetMetaInfo.class)) setGettingMetaInfo(true);
        if (ou.contains(OptionRealtime.class)) setRealtimeReplay(true);
        if (ou.contains(OptionSlowMotion.class)) {
            setRealtimeReplay(true);
            setSlowdown(ou.get(OptionSlowMotion.class).getFactor());
        }

        if (listener == null && !isGettingMetaInfo()) {
            System.out.println("no listener provided");
            return;
        }

        // Create replay thread
        final Thread t = new Thread(new Runnable() {

            private boolean hasMore = true;

            @SuppressWarnings("null")
            public void run() {
                try {
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
                    @SuppressWarnings("unused")
                    long previousExecutionTime = 0;
                    while (this.hasMore) {
                        AbstractSessionEvent event = null;

                        try {
                            event = (AbstractSessionEvent) loadFromStream(SessionReplayImpl.this.in);
                            if (previousEvent == null) {
                                SessionReplayImpl.this.firstEventtime = event.originalEventTime;
                                previousEvent = event;
                            }
                            SessionReplayImpl.this.currentEventime = event.originalEventTime;
                            // Dont process if filtered
                            if (!SessionReplayImpl.this.toFilter.contains(event.getClass())) {
                                if (!isGettingMetaInfo()) {
                                    listener.nextEvent(event);

                                    // Can be switched off, to make replay as fast as possible. 
                                    if (SessionReplayImpl.this.realtimeReplay && !isGettingMetaInfo()) {
                                        // Sleep till next event
                                        long delta = event.originalEventTime - previousEvent.originalEventTime;
                                        if (delta <= 0) {
                                            delta = 0;
                                        }

                                        try {
                                            Thread.sleep(delta * SessionReplayImpl.this.slowdownFactor);
                                        } catch (final InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    previousEvent = event;
                                } else {
                                    if (event instanceof ScreenSizeEvent) {
                                        SessionReplayImpl.this.screenSize = ((ScreenSizeEvent) event).screenSize;
                                        //                                                                                System.out.println(event);
                                    }
                                    if (event instanceof PropertyEvent) {
                                        SessionReplayImpl.this.propertyMap.put(((PropertyEvent) event).key, ((PropertyEvent) event).value);
                                        //                                                                                System.out.println(event);
                                    }
                                }
                            }
                        } catch (EOFException e) {
                            this.hasMore = false;
                            if (isGettingMetaInfo()) {
                                SessionReplayImpl.this.realtimeDuration = (SessionReplayImpl.this.currentEventime - SessionReplayImpl.this.firstEventtime);
                                //System.out.println("realtime duration: " + SessionReplayImpl.this.realtimeDuration + " ms");
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
                        barrier.await();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        t.setDaemon(true);
        t.start();

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

    /**
     * @param inStream
     * @return .
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object loadFromStream(final ObjectInputStream inStream) throws IOException,
                                                                  ClassNotFoundException {
        return (inStream.readObject());
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getProperties()
     */
    public Map<String, String> getProperties(String... properties) {
        waitForFinish();
        return this.propertyMap;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getScreenSize()
     */
    public Dimension getScreenSize() {
        waitForFinish();
        return this.screenSize;
    }

    /**
     * @return the gettingMetaInfo
     */
    boolean isGettingMetaInfo() {
        return this.gettingMetaInfo;
    }

    /**
     * @param gettingMetaInfo the gettingMetaInfo to set
     */
    void setGettingMetaInfo(final boolean gettingMetaInfo) {
        this.gettingMetaInfo = gettingMetaInfo;
    }

    /**
     * @return the duration of the original session
     */
    public long getRealtimeDuration() {
        return this.realtimeDuration;
    }

    /**
     * @param fixationDisplacementRegions
     */
    public synchronized void setFixationDisplacementRegions(
                                                            final List<DisplacementRegion> fixationDisplacementRegions) {
        this.fixationDisplacementRegions = fixationDisplacementRegions;
    }

    /**
     * @return .
     */
    public synchronized List<DisplacementRegion> getFixationDisplacementRegions() {
        return this.fixationDisplacementRegions;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getDisplacements()
     */
    @Override
    public List<DisplacementRegion> getDisplacements() {
        return new ArrayList<DisplacementRegion>();
    }

}
