/*
 * MasterLoggingHandler.java
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
package de.dfki.km.text20.browserplugin.browser.browserplugin.impl;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Master logging handler
 *
 * @author rb
 */
public class MasterLoggingHandler {

    private final FileHandler fileHandler;

    /**
     * Construct a new logging handler
     *
     * @param root
     * @param level
     * @throws SecurityException
     * @throws IOException
     */
    public MasterLoggingHandler(final String root, Level level) throws SecurityException,
                                                               IOException {
        this.fileHandler = new FileHandler(root + "/java.logging.bsfree.txt");

        // The output should appear in the files
        //Logger.getLogger("").addHandler(this.fh1);
        //Logger.getLogger("").addHandler(this.fh2);
        Logger.getLogger("").addHandler(this.fileHandler);

        if (level == null) {
            // Set configuration for logger.
            Logger.getLogger("").setLevel(Level.ALL);
            Logger.getLogger("net.xeoh").setLevel(Level.FINE);
            Logger.getLogger("java").setLevel(Level.WARNING);

            // Set all console handlers to shup up except for important messages
            final Handler[] handlers = Logger.getLogger("").getHandlers();
            for (final Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    final ConsoleHandler c = (ConsoleHandler) handler;
                    c.setLevel(Level.FINE);
                }
            }
        } else {
            // Set configuration for logger.
            Logger.getLogger("").setLevel(level);
            Logger.getLogger("java").setLevel(Level.WARNING);

            // Set all console handlers to shup up except for important messages
            final Handler[] handlers = Logger.getLogger("").getHandlers();
            for (final Handler handler : handlers) {
                if (handler instanceof ConsoleHandler) {
                    final ConsoleHandler c = (ConsoleHandler) handler;
                    c.setLevel(level);
                }
            }
        }

    }

    /**
     * Closes all logging files
     */
    public void shutdown() {
        this.fileHandler.flush();
        this.fileHandler.close();
    }
}
