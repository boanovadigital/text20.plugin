/*
 * AbstractLoader.java
 * 
 * Copyright (c) 2010, Ralf Biedert All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
 * @author rb
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
