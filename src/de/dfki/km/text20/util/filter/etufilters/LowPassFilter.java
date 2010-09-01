package de.dfki.km.text20.util.filter.etufilters;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Eugen Massini
 */
public final class LowPassFilter {
    /**
     * Apply a Low-pass filter on source. Resulting is in @a dest.
     * @param <T> 
     * 
     * @param dest Contains filtered Points. Should be an array of same size as source. 
     * @param source
     * @param dt
     * @param rc
     */
    public static final <T extends Point2D> void apply(final List<T> dest,
                                                       final List<T> source,
                                                       final double dt, final double rc) {

        final double alpha = dt / (rc + dt);

        assert !source.isEmpty();
        assert dest.size() == source.size();

        dest.get(0).setLocation(source.get(0));

        for (int i = 1; i < source.size(); ++i) {
            dest.get(i).setLocation(dest.get(i - 1).getX() + alpha * (source.get(i).getX() - dest.get(i - 1).getX()), dest.get(i - 1).getY() + alpha * (source.get(i).getY() - dest.get(i - 1).getY()));
        }
    }
}
