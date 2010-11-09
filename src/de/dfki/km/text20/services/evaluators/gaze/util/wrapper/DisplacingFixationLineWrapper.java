/*
 * DisplacingFixationLineWrapper.java
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
package de.dfki.km.text20.services.evaluators.gaze.util.wrapper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixationline.FixationLine;

/**
 * Creates a wrapper that tweaks a Fixation Line 
 * 
 * @author rb
 */
public class DisplacingFixationLineWrapper extends FixationLineWrapper {

    /** */
    private final Point[] displacements;

    /**
     * @param originalLine
     * @param displacements 
     */
    public DisplacingFixationLineWrapper(final FixationLine originalLine,
                                         final Point... displacements) {
        super(originalLine);

        this.displacements = displacements;

        if (displacements.length != originalLine.getFixations().size())
            throw new IllegalArgumentException("Need as many displacements as fixationsFS>");
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.services.gazeevaluator.util.wrapper.FixationLineWrapper#getFixations()
     */
    @Override
    public List<Fixation> getFixations() {
        final List<Fixation> rval = new ArrayList<Fixation>();

        int ctr = 0;

        for (final Fixation fixation : this.originalLine.getFixations()) {
            rval.add(new DisplacingFixationWrapper(fixation, this.displacements[ctr++]));
        }

        return rval;
    }
}
