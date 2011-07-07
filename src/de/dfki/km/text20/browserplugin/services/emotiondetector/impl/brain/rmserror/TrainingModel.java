/*
 * TrainingModel.java
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
                    double value = e.getReadings()[1];
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
                    double value = e.getReadings()[0];
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
                    sum += e.getReadings()[4];
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
                values[0] += e.getReadings()[0];
                values[1] += e.getReadings()[1];
                values[2] += e.getReadings()[4];
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
