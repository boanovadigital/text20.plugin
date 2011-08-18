/*
 * FixationsUtil.java
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
package de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.util;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Point;
import java.util.List;

import net.jcores.jre.CommonCore;
import net.jcores.jre.cores.CoreNumber;
import net.jcores.jre.cores.CoreObject;
import net.jcores.jre.cores.adapter.AbstractAdapter;
import net.jcores.jre.interfaces.functions.F1;
import net.jcores.jre.utils.internal.Wrapper;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.Fixation;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationDummy;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.fixation.util.FixationsUtil;
import de.dfki.km.text20.services.evaluators.gaze.listenertypes.saccade.Saccade;

/**
 * Methods regarding as set of {@link Saccade} objects.
 *
 * @author Ralf Biedert
 * @since 1.4
 */
public class SaccadesUtil extends CoreObject<Saccade> {

    /**  */
    private static final long serialVersionUID = -2238573022598996299L;

    /**
     * Creates a new saccades wrapper.
     * 
     * @param saccades The saccades to wrap.
     */
    public SaccadesUtil(final List<Saccade> saccades) {
        this(Wrapper.convert(saccades, Saccade.class));
    }
  

    /**
     * Creates a new saccades wrapper.
     * 
     * @param saccades The saccades to wrap.
     */
    public SaccadesUtil(final Saccade ... saccades) {
        super($, saccades);
    }
    
    /**
     * Creates a new saccades wrapper.
     * 
     * @param c
     * @param adapter
     */
    public SaccadesUtil(CommonCore c, AbstractAdapter<Saccade> adapter) {
        super(c, adapter);
    }

  
    /**
     * Returns the lenghts of all saccades.
     * 
     * @return The lenghts of all saccades.
     * @since 1.4
     */
    public CoreNumber lenghts() {
        return map(new F1<Saccade, Double>() {
            @SuppressWarnings("boxing")
            @Override
            public Double f(Saccade x) {
                return new SaccadeUtil(x).getLength();
            }
        }).as(CoreNumber.class);
    }
    
    
    /**
     * Returns the angles of all saccades.
     * 
     * @return The angles of all saccades.
     * @since 1.4
     */
    public CoreNumber angles() {
        return map(new F1<Saccade, Double>() {
            @SuppressWarnings("boxing")
            @Override
            public Double f(Saccade x) {
                return new SaccadeUtil(x).getAngle();
            }
        }).as(CoreNumber.class);
    }
    
    
    /**
     * Returns all start fixations. 
     * 
     * @return A CoreObject with all start positions.
     */
    public CoreObject<Fixation> starts() {
        return new CoreObject<Fixation>(this.commonCore, map(new F1<Saccade, Fixation>() {
            @Override
            public Fixation f(Saccade x) {
               return x.getStart();
            }
        }).unsafeadapter());
    }
    
    
    
    /**
     * Returns all start fixations. 
     * 
     * @return A CoreObject with all start positions.
     */
    public CoreObject<Fixation> ends() {
        return new CoreObject<Fixation>(this.commonCore, map(new F1<Saccade, Fixation>() {
            @Override
            public Fixation f(Saccade x) {
               return x.getEnd();
            }
        }).unsafeadapter());
    }
    

    
    /**
     * Computes the independent median saccade of all enclosed 
     * saccades. 
     * 
     * @since 1.4
     * @return The median saccade.
     */
    public Saccade independentMedian() {
        FixationsUtil s = starts().as(FixationsUtil.class);
        FixationsUtil e = ends().as(FixationsUtil.class);

        Point start = s.independentMedian();
        Point end = e.independentMedian();
        
        long st = s.duration() / s.size();
        long et = e.duration() / e.size();

        FixationDummy a = new FixationDummy().simulate(start, st);
        FixationDummy b = new FixationDummy().simulate(end, et);
        
        return new SaccadeDummy(a, b);
    }
}
