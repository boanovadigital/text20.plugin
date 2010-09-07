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
        this.events = new ArrayList<BrainTrackingEvent>();
        this.avgSimple = new HashMap<String, Double>();
        this.avgComplex = new HashMap<String, double[]>();
    }

    @SuppressWarnings("boxing")
    public void calculateAvgSimple(String emotion) {
        if (emotion.equals("happy")) {
            double emoAvg;
            double sum = 0;
            int cnt = 0;
            synchronized (this.events) {
                for (BrainTrackingEvent e : this.events) {
                    double value = e.getValue("channel:smile");
                    if (value != 0) {
                        sum += value;
                        cnt++;
                    }
                }
                if (cnt == 0) return;
                emoAvg = sum / cnt;
                this.events.clear();
            }
            this.avgSimple.put(emotion, emoAvg);
            return;
        }

        if (emotion.equals("doubt")) {
            double emoAvg;
            double sum = 0;
            int cnt = 0;
            synchronized (this.events) {
                for (BrainTrackingEvent e : this.events) {
                    double value = e.getValue("channel:furrow");
                    if (value != 0) {
                        sum += value;
                        cnt++;
                    }
                }
                if (cnt == 0) return;
                emoAvg = sum / cnt;
                this.events.clear();
            }
            this.avgSimple.put(emotion, emoAvg);
            return;
        }

        if (emotion.equals("interested") || emotion.equals("bored")) {
            double emoAvg;
            double sum = 0;
            synchronized (this.events) {
                for (BrainTrackingEvent e : this.events) {
                    sum += e.getValue("channel:engagement");
                }

                if (this.events.size() == 0) return;
                emoAvg = sum / this.events.size();
                this.events.clear();
            }

            this.avgSimple.put(emotion, emoAvg);
            return;
        }
    }

    public void calculateAvgComplex(String emotion) {
        double[] values = new double[3];
        int cnt = 0;
        synchronized (this.events) {
            cnt = this.events.size();
            for (BrainTrackingEvent e : this.events) {
                values[0] += e.getValue("channel:furrow");
                values[1] += e.getValue("channel:smile");
                values[2] += e.getValue("channel:engagement");
            }

            this.events.clear();
        }

        for (double d : values)
            d = d / cnt;

        this.avgComplex.put(emotion, values);
    }

    public void addEvent(BrainTrackingEvent event) {
        synchronized (this.events) {
            this.events.add(event);
        }
    }

    public void clearEvents() {
        synchronized (this.events) {
            this.events.clear();
        }
    }
}
