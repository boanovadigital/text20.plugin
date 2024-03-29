/*
 * PlainFileLoader.java
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

import static net.jcores.jre.CoreKeeper.$;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Ralf Biedert
 *
 */
public class PlainFileLoader extends AbstractLoader {

    /**
     * @param file
     */
    public PlainFileLoader(File file) {
        super(file);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.AbstractLoader#getSessionInputStream()
     */
    @Override
    public InputStream getSessionInputStream() {
        try {
            return new FileInputStream(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.text20.browserplugin.services.sessionrecorder.impl.xstream.loader.AbstractLoader#getFile(java.lang.String)
     */
    @Override
    public InputStream getFile(String name) {
        return $(this.file.getParent() + "/" + name).file().input().get(0);
    }
}
