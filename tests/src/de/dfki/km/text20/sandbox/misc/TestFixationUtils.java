/*
 * TestFixationUtils.java
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
package de.dfki.km.text20.sandbox.misc;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Point;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationDummy;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationsUtil;

/**
 * @author Ralf Biedert
 *
 */
public class TestFixationUtils {
    /**
     * @param args
     * @throws URISyntaxException
     * @throws IOException 
     */
    public static void main(final String[] args) throws URISyntaxException, IOException {
        final Fixation f[] = new Fixation[] { f(100, 100), f(200, 200), null, f(300, 300) };
        final List<Fixation> fl = Arrays.asList(f);

        final FixationsUtil fu = new FixationsUtil(fl);

        $(fu.saccades()).print();
    }

    /**
     * @param x
     * @param y
     * @return .
     */
    public static Fixation f(final int x, final int y) {
        return new FixationDummy().simulate(new Point(x, y), 100);
    }
}
