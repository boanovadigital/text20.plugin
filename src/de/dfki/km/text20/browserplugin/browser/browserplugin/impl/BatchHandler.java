/*
 * BatchHandler.java
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author rb
 *
 */
public class BatchHandler {
    /** */
    private final BrowserPluginImpl browserAPI;

    /** */
    final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * @param browserAPI
     */
    public BatchHandler(BrowserPluginImpl browserAPI) {
        this.browserAPI = browserAPI;
    }

    /**
     * @param call
     */
    public void batch(String call) {
        
        // Check where the actual call name ends
        final int indexOf = call.indexOf("(");
        if (indexOf < 0) {
            this.logger.warning("Batch: Wrong call syntax!");
            return;
        }

        // Extract method names and parameters
        final String method = call.substring(0, indexOf);
        final String[] arguments = call.substring(call.indexOf("(") + 1, call.length() - 1).split(";");
        final ArrayList<String[]> splittedArguments = new ArrayList<String[]>();
        for (final String argument : arguments) {
            splittedArguments.add(argument.split(","));
        }

        // Now check which call we have ...
        if (method.equals("simpleBenchmark")) {
            for (int i = 0; i < splittedArguments.size(); i++) {
                final String[] argument = splittedArguments.get(i);
                if (argument.length == 4) {

                    this.browserAPI.simpleBenchmark(argument[0], argument[1], argument[2], argument[3]);

                } else {
                    this.logger.warning("Batch: Wrong number of elements!");
                }
            }
        }

        if (method.equals("updateElementFlag")) {
            for (int i = 0; i < splittedArguments.size(); i++) {
                final String[] argument = splittedArguments.get(i);
                if (argument.length == 3) {
                    boolean bool;
                    if (argument[2].equals("true")) {
                        bool = true;
                    } else if (argument[2].equals("false")) {
                        bool = false;
                    } else {
                        this.logger.warning("Batch: Not a boolean!");
                        return;
                    }
                    this.browserAPI.updateElementFlag(argument[0], argument[1], bool);
                } else {
                    this.logger.warning("Batch: Wrong number of elements!");
                }
            }
        }

        if (method.equals("updateElementMeta")) {
            for (int i = 0; i < splittedArguments.size(); i++) {
                final String[] argument = splittedArguments.get(i);
                if (argument.length == 3) {
                    this.browserAPI.updateElementMetaInformation(argument[0], argument[1], argument[2]);
                } else {
                    this.logger.warning("Batch: Wrong number of elements!");
                }
            }
        }

        if (method.equals("updateElementGeometry")) {
            for (int i = 0; i < splittedArguments.size(); i++) {
                final String[] argument = splittedArguments.get(i);
                if (argument.length == 7) {
                    try {

                        String type = null;
                        String content = null;

                        try {
                            if (!argument[1].equals(".null")) {
                                type = URLDecoder.decode(argument[1], "UTF-8");
                            }
                            if (!argument[2].equals(".null")) {
                                content = URLDecoder.decode(argument[2], "UTF-8");
                            }
                        } catch (final UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        this.browserAPI.updateElementGeometry(argument[0], type, content, Integer.parseInt(argument[3]), Integer.parseInt(argument[4]), Integer.parseInt(argument[5]), Integer.parseInt(argument[6]));

                    } catch (final NumberFormatException e) {
                        this.logger.warning("Batch: Not a number");
                    }
                } else {
                    this.logger.warning("Batch: Wrong number of elements!");
                }
            }
        }
    }

}
