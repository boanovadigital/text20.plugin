/*
 * TestXStreamConverter.java
 * 
 * Copyright (c) 2010, Arman Vartan, DFKI. All rights reserved.
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
package de.dfki.km.text20.sandbox.misc;

import java.awt.Point;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.thoughtworks.xstream.XStream;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.EyeTrackingEventContainer;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.converter.EyeTrackingEventContainerConverter;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEvent;
import de.dfki.km.text20.services.trackingdevices.eyes.EyeTrackingEventValidity;

/**
 * @author Arman Vartan
 *
 */
public class TestXStreamConverter {

    public static void main(final String[] args) throws FileNotFoundException, IOException {

        final int numberOfSamples = 1;
        final int runs = 1;

        final List<EyeTrackingEventContainer> samples = new ArrayList<EyeTrackingEventContainer>(numberOfSamples);

        long start = 0;
        long stop = 0;

        // ##################

        System.out.print("Generating " + numberOfSamples + " sample data ");
        start = System.currentTimeMillis();
        for (int i = 0; i < numberOfSamples; i++) {
            samples.add(new EyeTrackingEventContainer(new EyeTrackingRandomEvent()));
        }
        stop = System.currentTimeMillis();
        System.out.println("in " + (double)(stop - start)/1000 + "s.\n");

        // ##################
        // ##################


        // Writer Test

        objectStreamWriteTest(samples, "benchmark1", runs, true);

        // Reader Test

        objectStreamReadTest("benchmark1", runs, true);

    }


    public static void objectStreamWriteTest(List<EyeTrackingEventContainer> samples, String filename, int runs, boolean withConverter) throws FileNotFoundException, IOException {

        final XStream xstream = new XStream();
        xstream.alias("EyeTrackingEventContainer", EyeTrackingEventContainer.class);

        if (withConverter) {
            xstream.registerConverter(new EyeTrackingEventContainerConverter());
        }

        long start = 0;
        long stop = 0;

        for (int i = 0; i < runs; i++) {
            System.out.print("ObjectOutputStream benchmark ");
            System.out.print((withConverter) ? "with converter " : "without converter ");
            final ObjectOutputStream out = xstream.createObjectOutputStream(new FileOutputStream(filename));

            start = System.currentTimeMillis();
            for (EyeTrackingEventContainer event : samples) {
                out.writeObject(event);
            }
            out.flush();
            stop = System.currentTimeMillis();

            System.out.println("took " + (double)(stop - start)/1000 + "s.");
            out.close();
        }

    }





    @SuppressWarnings("unused")
    public static void objectStreamReadTest(String filename, int runs, boolean withConverter) throws FileNotFoundException, IOException {
        final XStream xstream = new XStream();
        xstream.alias("EyeTrackingEventContainer", EyeTrackingEventContainer.class);

        if (withConverter) {
            xstream.registerConverter(new EyeTrackingEventContainerConverter());
        }

        long start = 0;
        long stop = 0;

        for (int i = 0; i < runs; i++) {
            System.out.print("ObjectInputStream benchmark ");
            System.out.print((withConverter) ? "with converter " : "without converter ");

            final ObjectInputStream in = xstream.createObjectInputStream(new FileInputStream(filename));
            start = System.currentTimeMillis();

            boolean finished = false;
            while (!finished) {
                try {
                    EyeTrackingEventContainer readObject = (EyeTrackingEventContainer) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException e) {
                    finished = true;
                }
            }
            stop = System.currentTimeMillis();

            System.out.println("took " + (double)(stop - start)/1000 + "s.");
            in.close();
        }

    }





    public static class EyeTrackingRandomEvent implements EyeTrackingEvent {

        private final Random rnd = new Random();

        @Override
        public long getObservationTime() {
            return this.rnd.nextLong();
        }

        @Override
        public boolean areValid(EyeTrackingEventValidity... validities) {
            return true;
        }

        @Override
        public Point getGazeCenter() {
            return new Point(this.rnd.nextInt(), this.rnd.nextInt());
        }

        @Override
        public float[] getHeadPosition() {
            float[] result = { this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat() };
            return result;
        }

        @Override
        public float getLeftEyeDistance() {
            return this.rnd.nextFloat();
        }

        @Override
        public float[] getLeftEyeGazePosition() {
            float[] result = { this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat() };
            return result;
        }

        @Override
        public float[] getRightEyeGazePosition() {
            float[] result = { this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat() };
            return result;
        }

        @Override
        public Point getRightEyeGazePoint() {
            return new Point(this.rnd.nextInt(), this.rnd.nextInt());
        }

        @Override
        public Point getLeftEyeGazePoint() {
            return new Point(this.rnd.nextInt(), this.rnd.nextInt());
        }

        @Override
        public float[] getLeftEyePosition() {
            float[] result = { this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat() };
            return result;
        }

        @Override
        public float getPupilSizeLeft() {
            return this.rnd.nextFloat();
        }

        @Override
        public float getPupilSizeRight() {
            return this.rnd.nextFloat();
        }

        @Override
        public float getRightEyeDistance() {
            return this.rnd.nextFloat();
        }

        @Override
        public float[] getRightEyePosition() {
            float[] result = { this.rnd.nextFloat(), this.rnd.nextFloat(), this.rnd.nextFloat() };
            return result;
        }

        /* (non-Javadoc)
         * @see de.dfki.km.text20.services.trackingdevices.common.TrackingEvent#getElapsedTime()
         */
        @Override
        public long getElapsedTime() {
            // TODO Auto-generated method stub
            return 0;
        }
    }


}

