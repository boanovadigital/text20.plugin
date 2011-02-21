/*
 * EyeTrackingEventContainerConverter.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.converter;

import java.awt.Point;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.EyeTrackingEventContainer;

/**
 * @author Vartan
 *
 */
public class EyeTrackingEventContainerConverter implements Converter {

    @SuppressWarnings("rawtypes")
    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(EyeTrackingEventContainer.class);
    }

    @Override
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        EyeTrackingEventContainer container = (EyeTrackingEventContainer) object;

        writer.startNode("originalEventTime");
        writer.setValue("" + container.getEventTime());
        writer.endNode();

        writer.startNode("combinedCenter");
            writer.startNode("x");
            writer.setValue("" + container.getGazeCenter().x);
            writer.endNode();

            writer.startNode("y");
            writer.setValue("" + container.getGazeCenter().y);
            writer.endNode();
        writer.endNode();

        writer.startNode("headPosition");
            for (float f : container.getHeadPosition()) {
                writer.startNode("float");
                writer.setValue("" + f);
                writer.endNode();
            }
        writer.endNode();

        writer.startNode("leftEyeDistance");
        writer.setValue("" + container.getLeftEyeDistance());
        writer.endNode();

        writer.startNode("leftEyePosition");
            for (float f : container.getLeftEyePosition()) {
                writer.startNode("float");
                writer.setValue("" + f);
                writer.endNode();
            }
        writer.endNode();

        writer.startNode("pupilSizeLeft");
        writer.setValue("" + container.getPupilSizeLeft());
        writer.endNode();

        writer.startNode("pupilSizeRight");
        writer.setValue("" + container.getPupilSizeRight());
        writer.endNode();

        writer.startNode("rightEyeDistance");
        writer.setValue("" + container.getRightEyeDistance());
        writer.endNode();

        writer.startNode("rightEyePosition");
            for (float f : container.getRightEyePosition()) {
                writer.startNode("float");
                writer.setValue("" + f);
                writer.endNode();
            }
        writer.endNode();

        writer.startNode("validity");
        writer.setValue("" + container.isValidity());
        writer.endNode();

        writer.startNode("version");
        writer.setValue("" + container.getVersion());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        EyeTrackingEventContainer event = new EyeTrackingEventContainer();
        String value = "";

        reader.moveDown();
        value = reader.getValue();
        event.setEventTime((value != null) ? Long.parseLong(value) : 0);
        reader.moveUp();

        reader.moveDown();
            Point combinedCenter = new Point();

            reader.moveDown();
            value = reader.getValue();
            combinedCenter.x = (value != null) ? Integer.parseInt(value) : 0;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            combinedCenter.y = (value != null) ? Integer.parseInt(value) : 0;
            reader.moveUp();

            event.setGazeCenter(combinedCenter);
        reader.moveUp();


        reader.moveDown();
            float[] headPosition = new float[3];

            reader.moveDown();
            value = reader.getValue();
            headPosition[0] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            headPosition[1] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            headPosition[2] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            event.setHeadPosition(headPosition);
        reader.moveUp();


        reader.moveDown();
            value = reader.getValue();
            event.setLeftEyeDistance((value != null) ? Float.parseFloat(value) : 0f);
        reader.moveUp();


        reader.moveDown();
            float[] leftEyePosition = new float[3];

            reader.moveDown();
            value = reader.getValue();
            leftEyePosition[0] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            leftEyePosition[1] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            leftEyePosition[2] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            event.setLeftEyePosition(leftEyePosition);
        reader.moveUp();


        reader.moveDown();
        value = reader.getValue();
        event.setPupilSizeLeft((value != null) ? Float.parseFloat(value) : 0f);
        reader.moveUp();

        reader.moveDown();
        value = reader.getValue();
        event.setPupilSizeRight((value != null) ? Float.parseFloat(value) : 0f);
        reader.moveUp();

        reader.moveDown();
        value = reader.getValue();
        event.setRightEyeDistance((value != null) ? Float.parseFloat(value) : 0f);
        reader.moveUp();


        reader.moveDown();
            float[] rightEyePosition = new float[3];

            reader.moveDown();
            value = reader.getValue();
            rightEyePosition[0] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            rightEyePosition[1] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            reader.moveDown();
            value = reader.getValue();
            rightEyePosition[2] = (value != null) ? Float.parseFloat(value) : 0f;
            reader.moveUp();

            event.setRightEyePosition(rightEyePosition);
        reader.moveUp();



        reader.moveDown();
        value = reader.getValue();
        event.setValidity((value != null) ? Boolean.parseBoolean(value) : true);
        reader.moveUp();


        reader.moveDown();
        value = reader.getValue();
        event.setVersion((value != null) ? Integer.parseInt(value) : 1);
        reader.moveUp();



        return event;
    }


}
