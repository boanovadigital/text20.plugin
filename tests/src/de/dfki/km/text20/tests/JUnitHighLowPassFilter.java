package de.dfki.km.text20.tests;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.dfki.km.text20.util.filter.etufilters.HighPassFilter;
import de.dfki.km.text20.util.filter.etufilters.LowPassFilter;

/**
 * @author rb
 */
public class JUnitHighLowPassFilter {
    List<Point2D.Double> dest = new ArrayList<Point2D.Double>();
    List<Point2D.Double> source = new ArrayList<Point2D.Double>();

    /**
     * 
     */
    @After
    public void after() {
        /*
        for(Point2D.Double p: this.source){
           System.out.println(p.x + " "+p.y);
        }
        System.out.println("-----------");
        //*/
        //*
        for (final Point2D.Double p : this.dest) {
            System.out.println(p.x + "; " + p.y);
        }
        //*/
    }

    /**
     * 
     */
    @Before
    public void clearDest() {
        for (int i = 0; i < 100; ++i) {
            final Point2D.Double p = new Point2D.Double(); //sin(,1*x)+,2*sin(4*x)
            p.setLocation(i * 6. / 100., Math.sin(.1 * i) + 0.2 * Math.sin(4 * i));
            this.source.add(p);

            this.dest.add(new Point2D.Double());
        }

        for (final Point2D.Double p : this.dest) {
            p.setLocation(0, 0);
        }

        for (final Point2D.Double p : this.source) {
            System.out.println(p.x + " " + p.y);
        }
    }

    /**
     * 
     */
    @Test
    public void highPassFilter() {
        final double dt = 1;
        final double rc = 0.9;
        HighPassFilter.apply(this.dest, this.source, dt, rc);
        System.out.println("highPassFilter: ");
    }

    /**
     * 
     */
    @Test
    public void lowPassFilter() {
        final double dt = 1;
        final double rc = 0.23;
        LowPassFilter.apply(this.dest, this.source, dt, rc);
        System.out.println("lowPassFilter: ");
    }
}
