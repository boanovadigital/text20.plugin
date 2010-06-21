/*
 * TrackingDeviceImpl.java
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
package de.dfki.km.text20.browserplugin.browser.browseradapter.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.Thread;
import net.xeoh.plugins.base.annotations.configuration.IsDisabled;
import net.xeoh.plugins.base.annotations.events.Init;
import de.dfki.km.text20.browserplugin.browser.browseradapter.BrowserAdapter;
import de.dfki.km.text20.browserplugin.browser.browseradapter.BrowserAdapterManager;

/**
 * 
 * @author rb
 *
 */
@IsDisabled
@PluginImplementation
public class BrowserAdapterManagerImpl implements BrowserAdapterManager {
    private final static int PORT = 55321;

    private final List<BrowserAdapter> adapter = new ArrayList<BrowserAdapter>();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Our server socket
     */
    ServerSocket serverSocket;

    /**
     * Init the server socket.
     * 
     * @throws IOException
     */
    @Init
    public void init() throws IOException {
        // 1. Create Server socket
        this.serverSocket = new ServerSocket(PORT);

        // 2. For each new connection, spawn a BrowserAdapter
        // (see below)
    }

    /**
     * Wait for new browser plugins
     */
    @Thread(isDaemonic = false)
    public void listenerThread() {
        // 2. For each new connection, spawn a BrowserAdapter
        while (true) {
            try {
                this.logger.fine("Waiting for a new client ");

                final Socket accept = this.serverSocket.accept();
                final BrowserAdapterImpl bai = new BrowserAdapterImpl(accept);

                this.logger.fine("Accepted a new client");

                bai.start();

                this.adapter.add(bai);
            } catch (final IOException e) {
                this.logger.warning("An error occured while waiting for browser plugin-requests");
                e.printStackTrace();
            }
        }
    }
}
