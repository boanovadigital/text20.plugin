/*
 * ObjectComparableMap.java
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
package de.dfki.km.text20.util.histogram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Manages mappings of the type object to number
 *
 * @author Ralf Biedert
 *
 * @param <K>
 * @param <V>
 */
public class ObjectComparableMap<K, V> extends HashMap<K, V> {

    /** */
    private static final long serialVersionUID = -4065055237132059858L;

    /** */
    private final Comparator<V> comparator;

    /**
     * @param entrySet
     * @param comp 
     * @param initialValue
     */
    public ObjectComparableMap(final Collection<K> entrySet, final Comparator<V> comp,
                               final V initialValue) {
        super();

        if (comp == null) throw new IllegalArgumentException();

        this.comparator = comp;

        for (final K k : entrySet) {
            put(k, initialValue);
        }
    }

    /**
     * Dumps the histogram to a file.
     *
     * @param string
     */
    @SuppressWarnings("unchecked")
    public void dumpTo(final String string) {
        final ObjectComparableMap<K, V> copy = (ObjectComparableMap<K, V>) clone();

        final StringBuilder sb = new StringBuilder();
        K p;

        while ((p = copy.getHighest()) != null) {
            sb.append(copy.get(p));
            sb.append(",");
            sb.append(p);
            sb.append("\n");
            copy.remove(p);
        }

        throw new NotImplementedException();
        //FileUtils.writeString(string, sb.toString());
    }

    /**
     * Returns the highest object.
     * 
     * @return .
     */
    public K getHighest() {
        if (size() == 0) return null;

        if (this.comparator == null) throw new IllegalArgumentException();

        // Get one element
        K rval = keySet().iterator().next();
        V highest = get(rval);

        for (final K k : keySet()) {
            final V c = get(k);

            if (this.comparator.compare(c, highest) > 0) {
                highest = c;
                rval = k;
            }
        }

        return rval;
    }

    /**
     * @param n
     * @return . 
     */
    @SuppressWarnings("unchecked")
    public List<K> getHighest(final int n) {
        final List<K> rval = new ArrayList<K>();

        final ObjectComparableMap<K, V> hcopy = (ObjectComparableMap<K, V>) clone();

        for (int i = 0; i < n; i++) {
            final K highest = hcopy.getHighest();

            if (highest == null) return rval;

            rval.add(highest);
            hcopy.remove(highest);
        }

        return rval;
    }

    /**
     * Returns the highest object.
     * 
     * @return .
     */
    public V getHighestValue() {
        if (size() == 0) return null;

        if (this.comparator == null) throw new IllegalArgumentException();

        // Get one element
        K rval = keySet().iterator().next();
        V highest = get(rval);

        for (final K k : keySet()) {
            final V c = get(k);

            if (this.comparator.compare(c, highest) > 0) {
                highest = c;
                rval = k;
            }
        }

        return highest;
    }

    /**
     * @param n
     * @return . 
     */
    @SuppressWarnings("unchecked")
    public List<V> getHighestValues(final int n) {
        final List<V> rval = new ArrayList<V>();

        final ObjectComparableMap<K, V> hcopy = (ObjectComparableMap<K, V>) clone();

        for (int i = 0; i < n; i++) {
            final K highestKey = hcopy.getHighest();
            final V highest = hcopy.getHighestValue();

            if (highest == null) return rval;

            rval.add(highest);
            hcopy.remove(highestKey);
        }

        return rval;
    }

    /**
     * Only puts a new value if the old value is smaller
     * 
     * @param key 
     * @param newValue      
     */
    public void putIfHigher(final K key, final V newValue) {
        final V v = get(key);

        if (this.comparator.compare(v, newValue) < 0) {
            put(key, newValue);
        }
    }

}
