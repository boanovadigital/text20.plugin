package de.dfki.km.text20.util.clustering;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import drasys.or.PairI;
import drasys.or.geom.KDTree;
import drasys.or.geom.geo.PointI;
import drasys.or.geom.rect2.Point;
import drasys.or.geom.rect2.Range;

/**
 * TODO: Unfinished.
 * 
 * 
 * @author Eugen Massini
 */
public final class DensityModelBasedClustering {

    private static class PointInfo {
        public int clusterId;
        @SuppressWarnings("unused")
        public double distToNN = Double.POSITIVE_INFINITY;
        private State state;

        PointInfo() {
            this.state = State.UNPROCESSED;
            this.clusterId = NO_ID;
        }

        // changeState can only change 'unprocessed' to 'processing'
        // ... cannot change 'processed' to 'processing'
        public void changeState(final State s) {
            if (this.state == State.UNPROCESSED && s == State.PROCESSING || this.state == State.PROCESSING && s == State.PROCESSED || this.state == State.UNPROCESSED && s == State.PROCESSED) {
                this.state = s;
            }
        }

        public State getState() {
            return this.state;
        }
    }

    private enum State { ///> describes state of a point
        PROCESSED, PROCESSING, UNPROCESSED
    }

    /** */
    public static final int NO_ID = -1;

    /**
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(final String[] args) {
        final KDTree tree = new KDTree();

        tree.put(new Point(2, 9), null);
        tree.put(new Point(3, 6), null);
        tree.put(new Point(1, 2), null);
        tree.put(new Point(6, 6), null);
        tree.put(new Point(5, 3), null);
        tree.put(new Point(9, 8), null);
        tree.put(new Point(8, 4), null);

        final Range r = new Range(3, 1, 10, 5);
        System.out.println("selected: " + tree.selectRange(r));

        final Enumeration<PairI> e = tree.selectedElements();
        PairI p = e.nextElement();
        for (; e.hasMoreElements(); p = e.nextElement()) {
            System.out.println("# " + p.getFirst());
        }
        //*/
    }

    @SuppressWarnings("unused")
    // just because the class is not already implemented
    private final double minimalUnit;

    private final Random rand = new Random();

    private final KDTree setOfPoints = new KDTree();

    /**
     * @param screenDiagonal
     */
    public DensityModelBasedClustering(final double screenDiagonal) {
        this.minimalUnit = screenDiagonal / 1024.0;
    }

    /**
     * @param p
     */
    public void addPoint(final PointI p) {
        this.setOfPoints.put(p, new PointInfo());
        // TODO: Range scaling
    }

    /**
     * @param points
     */
    public void addPoints(final PointI[] points) {
        for (final PointI p : points) {
            addPoint(p);
        }
    }

    /**
     * 
     */
    public void apply() {
        int clusterId = NO_ID + 1;

        PairI coreP = getCorePoint();
        while (coreP != null && this.setOfPoints.size() > 0) {
            final List<PairI> activeLink = new ArrayList<PairI>();
            activeLink.add(coreP);
            expand(activeLink, clusterId);
            coreP = getCorePoint();
            ++clusterId;
        }
    }

    // if q is already in activeLink or its processing state is 'processed'... 
    // ... then add procedure will do nothing
    /**
     * @param activeLink
     * @param q
     * @param p
     */
    private void addToActiveLink(final List<PairI> activeLink, final PairI q,
                                 final PairI p) {
        if (((PointInfo) q.getSecond()).getState() == State.PROCESSED) return;
        if (activeLink.contains(q)) return;

        // TODO: impl
    }

    /**
     * @param activeLink
     * @param clusterId
     */
    private void expand(final List<PairI> activeLink, final int clusterId) {
        if (activeLink.isEmpty()) return;

        final PairI keyValue = this.setOfPoints.getNearestNeighborTo((PointI) activeLink.get(0).getFirst());
        final PointI p = (PointI) keyValue.getFirst();
        final PointInfo info = (PointInfo) keyValue.getSecond();

        final List<PairI> temp = retrieveAllNN(p);

        if (!isInSameDensityModel(p, (PointI) temp.get(0).getFirst())) { // p is not in the same dense cluster as activeLink
            activeLink.remove(keyValue);
            if (info.clusterId == NO_ID) {
                info.changeState(State.UNPROCESSED);
            }
        } else { // p belongs to the dense cluster of activeLink
            for (final PairI q : temp) {
                addToActiveLink(activeLink, q, keyValue);
                ((PointInfo) q.getSecond()).changeState(State.PROCESSING);
            }
            markCid(p, info);
            info.changeState(State.PROCESSED);
            activeLink.remove(p);
        } // end else
        expand(activeLink, info.clusterId);
    }

    private PairI getCorePoint() {
        PairI coreKV = null;
        final int size = this.setOfPoints.size();
        if (size == 0) return null;

        PointInfo coreInfo;
        do {
            final int point = this.rand.nextInt(size);

            // TODO: search for core point and make a record of the distance between the core point
            //		 and its nearest neighbor as a benchmark for later clustering procedure

            // For the first purpose just select a point randomly and call it core point :)

            coreKV = getElement(point);
            if (coreKV == null) return null;
            coreInfo = (PointInfo) coreKV.getSecond();
        } while (coreInfo.getState() != State.UNPROCESSED);

        // set distance o the nearest neighbor of core point
        final PointI coreP = (PointI) coreKV.getFirst();
        final PairI nnKV = this.setOfPoints.getNearestNeighborTo(coreP);
        final PointI nnP = (PointI) nnKV.getFirst();

        coreInfo.distToNN = nnP.getDistanceTo(coreP);

        return coreKV;
    }

    /**
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    private PairI getElement(final int index) {
        final Enumeration<PairI> points = this.setOfPoints.elements();
        if (!points.hasMoreElements()) return null;

        int count = 0;
        for (PairI kv = points.nextElement(); points.hasMoreElements(); kv = points.nextElement()) {
            if (count == index) return kv;
            count++;
        }

        return null;
    }

    /**
     * @param p
     * @param pointI
     * @return
     */
    private boolean isInSameDensityModel(final PointI p, final PointI pointI) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @param p
     * @param info
     */
    private void markCid(final PointI p, final PointInfo info) {
        // TODO Auto-generated method stub

    }

    /**
     * @param p
     * @return
     */
    private List<PairI> retrieveAllNN(final PointI p) {

        /*List<PairI> result = new List<PairI>();
        
        PointI nn = this.setOfPoints.selectNearestNeighbors(arg0, arg1)
        if (nn )
        */
        return null;
    }
}
