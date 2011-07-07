/*
 * ZIPLoader.java
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
package de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * @author Ralf Biedert
 *
 */
public class ZIPLoader extends AbstractLoader {

    /** The ZIP file we represent */
    private ZipFile zipFile;

    /** Primary entry (replay info) */
    private ZipEntry selected;

    private String prefix;

    /**
     * @param file
     */
    public ZIPLoader(File file) {
        super(file);

        try {
            this.zipFile = new ZipFile(this.file);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.AbstractLoader#getSessionInputStream()
     */
    @Override
    public InputStream getSessionInputStream() {

        try {
            final Enumeration<? extends ZipEntry> entries = this.zipFile.entries();

            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.getName().endsWith(".xstream")) this.selected = zipEntry;
            }

            final String name = this.selected.getName();
            final int i = name.lastIndexOf("/");

            this.prefix = name.substring(0, i) + "/";

            return this.zipFile.getInputStream(this.selected);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.AbstractLoader#getFile(java.lang.String)
     */
    @Override
    public InputStream getFile(String name) {
        try {
            return this.zipFile.getInputStream(this.zipFile.getEntry(this.prefix + name));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
