/*
 * TestSerialization.java
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
import java.util.List;

import de.dfki.km.text20.browserplugin.services.sessionrecorder.events.AbstractSessionEvent;
import de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.plainxml.SessionRecordImpl;

/**
 * @author Ralf Biedert
 *
 */
public class TestSerialization {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final SessionRecordImpl sr = new SessionRecordImpl(new Dimension(800, 600));
        sr.newImage("xx");
        sr.updateDocumentViewport(new Point(200, 200));
        sr.updateElementGeometry("id", "type", "content", new Rectangle(12, 3, 4, 5));
        sr.writeTo("test.xml");

        final SessionRecordImpl sr2 = SessionRecordImpl.loadFrom("test.xml");
        System.out.println(sr2);
        final List<AbstractSessionEvent> allEvents = sr2.getAllEvents();
        for (final AbstractSessionEvent event : allEvents) {
            System.out.println(event);
        }
    }
}
