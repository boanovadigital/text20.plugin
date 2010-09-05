package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml;

import java.awt.Dimension;
import java.io.File;
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

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#replay(de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.ReplayListener, de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.options.ReplayOption[])
     */
    @Override
    public void replay(final ReplayListener listener, final ReplayOption... options) {

        final List<AbstractSessionEvent> allEvents = this.record.getAllEvents();
        final CyclicBarrier barrier = new CyclicBarrier(2);

        final OptionUtils<ReplayOption> ou = new OptionUtils<ReplayOption>(options);
        if (ou.contains(OptionGetMetaInfo.class)) setGettingMetaInfo(true);
        if (ou.contains(OptionRealtime.class)) setRealtimeReplay(true);
        if (ou.contains(OptionSlowMotion.class)) {
            setRealtimeReplay(true);
            setSlowdown(ou.get(OptionSlowMotion.class).getFactor());
        }

        // Create replay thread
        final Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                AbstractSessionEvent previousEvent = null;

                while (allEvents.size() > 0) {
                    AbstractSessionEvent event = null;

                    event = allEvents.get(0);
                    allEvents.remove(0);

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
                }

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
     * Makes other threads wait for this element to be finished.
     */
    Lock finishedLock = new ReentrantLock();

    /** Should we print timing information */
    @SuppressWarnings("unused")
    private final boolean printTiming = true;

    /** If the replay is in realtime, or as fast as possible */
    boolean realtimeReplay = false;

    /**
     * controls the behaviour of the replay method if true, only the property and screen
     * size 'events' are fetched and stored if false, the listeners will work
     */
    private boolean gettingMetaInfo = false;

    /** How far to slow down "realtime" */
    int slowdownFactor = 1;

    /** List of events to filter */
    final List<Class<? extends AbstractSessionEvent>> toFilter = new ArrayList<Class<? extends AbstractSessionEvent>>();

    /** */
    Map<String, String> propertyMap = new HashMap<String, String>();

    /** the recorded screen size */
    Dimension screenSize;

    /** the file to replay */
    private File file;

    /**  */
    private SessionRecordImpl record;

    /**  */
    protected long firstEventtime;

    /**  */
    protected long currentEventime;

    /**  */
    protected long realtimeDuration;

    /**
     * @return .
     */
    public SessionRecordImpl getRecordProxy() {
        return this.record;
    }

    /**
     * @param file
     */
    public SessionReplayImpl(final File file) {
        this.file = file;
        this.record = SessionRecordImpl.loadFrom(this.file.getAbsolutePath(), true);
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
     * @param realtimeReplay
     *            the realtimeReplay to set
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

    /**
     * Waits until the replay thread has finished.
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

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getProperties()
     */
    @Override
    public Map<String, String> getProperties(String... properties) {
        return this.record.getProperties();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.sessionrecorder.SessionReplay#getScreenSize()
     */
    @Override
    public Dimension getScreenSize() {
        return this.record.getScreenSize();
    }

    /**
     * @return the gettingMetaInfo
     */
    boolean isGettingMetaInfo() {
        return this.gettingMetaInfo;
    }

    /**
     * @param gettingMetaInfo
     *            the gettingMetaInfo to set
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
     * @return .
     */
    public File getFile() {
        return this.file;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.SessionReplay#getDisplacements()
     */
    @Override
    public List<DisplacementRegion> getDisplacements() {
        return (this.record.fixationDisplacementRegions != null) ? this.record.fixationDisplacementRegions : new ArrayList<DisplacementRegion>();
    }

}
