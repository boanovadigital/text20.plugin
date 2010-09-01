package de.dfki.km.text20.util.filter.etufilters;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author Eugen Massini
 *
 */
public final class HighPassFilter {

    /**
     * Apply a high-pass filter on source. Result is in @a dest.
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
        final double alpha = rc / (rc + dt);

        assert !source.isEmpty();
        assert dest.size() == source.size();

        dest.get(0).setLocation(source.get(0));

        for (int i = 1; i < source.size(); ++i) {
            dest.get(i).setLocation(alpha * (dest.get(i - 1).getX() + source.get(i).getX() - source.get(i - 1).getX()), alpha * (dest.get(i - 1).getY() + source.get(i).getY() - source.get(i - 1).getY()));
        }
    }
}
