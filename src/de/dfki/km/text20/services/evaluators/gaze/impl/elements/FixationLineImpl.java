/*
 * FixationLineImpl.java
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
package de.dfki.km.text20.services.evaluators.gaze.impl.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;

/**
 * @author rb
 */
public class FixationLineImpl implements FixationLine {
    /** */
    final List<Fixation> allFixations;

    /**
     *
     */
    public FixationLineImpl() {
        this.allFixations = new ArrayList<Fixation>();
    }

    /**
     * @param fixations
     */
    public FixationLineImpl(final List<Fixation> fixations) {
        this.allFixations = fixations;
    }

    /**
     *
     * Insert a new fixation into this line.
     *
     * @param fixation
     */
    public void addFixation(final Fixation fixation) {
        this.allFixations.add(fixation);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.sandbox.services.gazeevaluator.listenertypes.fixationline.FixationLine#getFixations()
     */
    @Override
    public List<Fixation> getFixations() {
        return Collections.unmodifiableList(this.allFixations);
    }

}
