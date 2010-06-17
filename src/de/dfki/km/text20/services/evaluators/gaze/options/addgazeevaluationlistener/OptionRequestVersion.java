/*
 * OptionRequestVersion.java
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
package de.dfki.km.text20.services.evaluators.gaze.options.addgazeevaluationlistener;

import de.dfki.km.text20.services.evaluators.gaze.GazeEvaluationListener;
import de.dfki.km.text20.services.evaluators.gaze.options.AddGazeEvaluationListenerOption;

/**
 * @author rb
 *
 */
public class OptionRequestVersion implements AddGazeEvaluationListenerOption {

    /** */
    private static final long serialVersionUID = -5981935295824298580L;

    /**
     * 
     */
    private final String author;

    /**
     * 
     */
    private final int version;

    /**
     * 
     */
    private final String[] capabilities;

    /** */
    private final Class<? extends GazeEvaluationListener<?>> listener;

    /**
     * @param listener
     * @param author 
     * @param version - Note: Any version larger than the given one is considered 
     * @param capabilities 
     */
    public OptionRequestVersion(Class<? extends GazeEvaluationListener<?>> listener,
                                String author, int version, String... capabilities) {
        this.listener = listener;
        this.author = author;
        this.version = version;
        this.capabilities = capabilities;
    }

    /**
     * @param listener
     * @param capabilities 
     */
    public OptionRequestVersion(Class<? extends GazeEvaluationListener<?>> listener,
                                String... capabilities) {
        this.listener = listener;
        this.author = null;
        this.version = -1;
        this.capabilities = capabilities;
    }

    /**
     * @return the version
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * @return the capabilities
     */
    public String[] getCapabilities() {
        return this.capabilities;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * @return the listener
     */
    public Class<? extends GazeEvaluationListener<?>> getListener() {
        return this.listener;
    }

}
