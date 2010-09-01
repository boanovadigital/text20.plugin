/*
 * ObjectNumberMap.java
 *
 * Copyright (c) 2008, Ralf Biedert, DFKI. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
