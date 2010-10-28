/*
 * TestTrackingClient.java
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author rb
 *
 */
public class TestZipReplay {
    /**
     * @param args
     * @throws URISyntaxException
     * @throws IOException 
     */
    public static void main(final String[] args) throws URISyntaxException, IOException {
        final ZipFile zipFile = new ZipFile("../Text 2.0 Experiment Results/quickskim.prestudy.2010/experiment.3.zip");
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();

        System.out.println(zipFile);

        ZipEntry selected = null;

        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = entries.nextElement();
            if (zipEntry.getName().endsWith(".xstream")) selected = zipEntry;
        }

        final InputStream inputStream = zipFile.getInputStream(selected);

        System.out.println(inputStream.read());
    }
}
