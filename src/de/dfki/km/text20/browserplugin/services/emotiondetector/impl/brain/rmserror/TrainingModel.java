package de.dfki.km.text20.browserplugin.services.emotiondetector.impl.brain.rmserror;

import java.util.ArrayList;
import java.util.HashMap;

import de.dfki.km.text20.services.trackingdevices.brain.BrainTrackingEvent;

public class TrainingModel {

    /** Contains the brain tracker events */
    private ArrayList<BrainTrackingEvent> events;

    /** Contains the average values to be used to calculate the root mean squared error - simple*/
    public HashMap<String, Double> avgSimple;

    /** Contains the average values to be used to calculate the root mean squared error - complex*/
    public HashMap<String, double[]> avgComplex;

    /** Constructor */
    public TrainingModel() {
        events = new ArrayList<BrainTrackingEvent>();
        avgSimple = new HashMap<String, Double>();
        avgComplex = new HashMap<String, double[]>();
    }

    public void calculateAvgSimple(String emotion) {
        if (emotion.equals("happy")) {
            double emoAvg;
            double sum = 0;
            int cnt = 0;
            synchronized (events) {
                for (BrainTrackingEvent e : events) {
                    double value = e.getValue("channel:smile");
                    if (value != 0) {
                        sum += value;
                        cnt++;
                    }
                }
                if (cnt == 0) return;
                emoAvg = sum / cnt;
                events.clear();
            }
            avgSimple.put(emotion, emoAvg);
            return;
        }

        if (emotion.equals("doubt")) {
            double emoAvg;
            double sum = 0;
            int cnt = 0;
            synchronized (events) {
                for (BrainTrackingEvent e : events) {
                    double value = e.getValue("channel:furrow");
                    if (value != 0) {
                        sum += value;
                        cnt++;
                    }
                }
                if (cnt == 0) return;
                emoAvg = sum / cnt;
                events.clear();
            }
            avgSimple.put(emotion, emoAvg);
            return;
        }

        if (emotion.equals("interested") || emotion.equals("bored")) {
            double emoAvg;
            double sum = 0;
            synchronized (events) {
                for (BrainTrackingEvent e : events) {
                    sum += e.getValue("channel:engagement");
                }
                if (events.size() == 0) return;
                emoAvg = sum / events.size();
                events.clear();
            }
            avgSimple.put(emotion, emoAvg);
            return;
        }
    }

    public void calculateAvgComplex(String emotion) {
        double[] values = new double[3];
        int cnt = 0;
        synchronized (events) {
            cnt = events.size();
            for (BrainTrackingEvent e : events) {
                values[0] += e.getValue("channel:furrow");
                values[1] += e.getValue("channel:smile");
                values[2] += e.getValue("channel:engagement");
            }
            events.clear();
        }

        for (double d : values)
            d = d / cnt;

        avgComplex.put(emotion, values);
    }

    public void addEvent(BrainTrackingEvent event) {
        synchronized (events) {
            events.add(event);
        }
    }

    public void clearEvents() {
        synchronized (events) {
            events.clear();
        }
    }
}
