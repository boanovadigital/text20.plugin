/*
 * TestSerializationStream.java
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
package de.dfki.km.text20.sandbox.misc;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.SessionStreamer;

/**
 * @author rb
 *
 */
public class TestSerializationStream {

    /**
      * @param args
      */
    @SuppressWarnings("null")
    public static void main(final String[] args) {
        String sessionRecordFileName = "ATStream.xml";
        XStream xstream = new XStream();
        BufferedReader br = null;

        final SessionStreamer streamer = new SessionStreamer(new Dimension(800, 600), sessionRecordFileName, null);
        for (int i = 0; i < 10; i++) {
            streamer.newImage("xx");
            streamer.updateDocumentViewport(new Point(200 + i, 200 - i));
            streamer.updateElementGeometry("id", "type", "content", new Rectangle(12, 3, 4, 5));
        }

        System.out.println("readsection-----------------");
        ObjectInputStream in = null;
        SessionStreamer.setAlias(xstream);

        try {
            br = new BufferedReader(new FileReader(sessionRecordFileName));
            in = xstream.createObjectInputStream(br);
            AbstractSessionEvent evt = null;
            while (true) {
                evt = SessionStreamer.loadFromStream(in);
                System.out.println(evt);
            }
        } catch (StreamException e) {
            System.out.println("end of data ...");
            try {
                in.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
