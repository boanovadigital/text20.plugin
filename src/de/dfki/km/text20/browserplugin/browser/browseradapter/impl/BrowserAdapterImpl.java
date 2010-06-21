/*
 * BrowserAdapterImpl.java
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import de.dfki.km.text20.browserplugin.browser.browseradapter.BrowserAdapter;

/**
 * Talks to a client.
 * 
 * @author rb
 */
public class BrowserAdapterImpl implements BrowserAdapter {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    final Socket connection;

    InputStream inputStream;

    OutputStream outputStream;

    /**
     * Constructed by the BrowserAdapterManager
     * @param connection
     */
    protected BrowserAdapterImpl(final Socket connection) {
        this.connection = connection;

        configureSocket();
    }

    /**
     * Start a new thread and listen
     */
    public void start() {
        final BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream));
        final PrintWriter pw = new PrintWriter(this.outputStream);

        final Thread t = new Thread(new Runnable() {
            public void run() {
                pw.print("init ok");
                pw.print("\n");
                pw.flush();

                @SuppressWarnings("unused")
                final int i = 0;

                while (true) {
                    try {
                        // If we have something to read
                        if (BrowserAdapterImpl.this.inputStream.available() > 0) {
                            // Recv. something
                            @SuppressWarnings("unused")
                            final String readLine = br.readLine();
                        }

                        // Send something
                        pw.print("ping");
                        pw.print("\n");
                        pw.flush();

                        if (BrowserAdapterImpl.this.connection.isClosed()) return;
                        if (!BrowserAdapterImpl.this.connection.isConnected()) return;

                        Thread.sleep(10);
                    } catch (final IOException e) {
                        e.printStackTrace();
                        return;
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        t.setDaemon(false);
        t.start();
    }

    /**
     * Setup socket options
     */
    private void configureSocket() {
        try {
            this.connection.setTcpNoDelay(true);
            this.inputStream = this.connection.getInputStream();
            this.outputStream = this.connection.getOutputStream();

        } catch (final SocketException e) {
            this.logger.warning("Error setting socket options!");
            e.printStackTrace();
        } catch (final IOException e) {
            this.logger.warning("Error getting streams!");
            e.printStackTrace();
        }
    }
}
