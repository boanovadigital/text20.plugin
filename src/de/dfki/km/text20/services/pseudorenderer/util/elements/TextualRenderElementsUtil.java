/*
 * TextualRenderElementUtil.java
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
package de.dfki.km.text20.services.pseudorenderer.util.elements;

import static net.jcores.jre.CoreKeeper.$;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import net.jcores.jre.CommonCore;
import net.jcores.jre.cores.CoreNumber;
import net.jcores.jre.cores.CoreObject;
import net.jcores.jre.cores.adapter.AbstractAdapter;
import net.jcores.jre.interfaces.functions.F1;
import net.jcores.jre.interfaces.functions.F1Object2Bool;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * Util functions for a textual render element.
 * 
 * @author Ralf Biedert
 * @since 1.4
 */
public class TextualRenderElementsUtil extends CoreObject<TextualRenderElement> {
    /** */
    private static final long serialVersionUID = -6738280239341865900L;

    /**
     * @param renderElement
     */
    public TextualRenderElementsUtil(TextualRenderElement ... renderElement) {
        super($, renderElement);
    }
    
    /**
     * @param renderElement
     */
    public TextualRenderElementsUtil(Collection<TextualRenderElement> renderElement) {
        super($, renderElement);
    }
    
    /**
     * @param c 
     * @param a
     */
    public TextualRenderElementsUtil(CommonCore c, AbstractAdapter<TextualRenderElement> a) {
        super($, a);
    }
    
    

    /**
     * Returns all available text IDs.
     * 
     * @return CoreNumber object.
     */
    public CoreNumber textIDs() {
        return map(new F1<TextualRenderElement, Number>() {
            @SuppressWarnings("boxing")
            @Override
            public Number f(TextualRenderElement x) {
                return x.getTextID();
            }
        }).unique().as(CoreNumber.class);
           
    }
    
    
    /**
     * Returns all available text IDs.
     * 
     * @param id 
     * @return CoreNumber object.
     */
    public TextualRenderElementsUtil textID(final int id) {
        return filter(new F1Object2Bool<TextualRenderElement>() {
            @Override
            public boolean f(TextualRenderElement x) {
                return x.getTextID() == id;
            }
        }).as(TextualRenderElementsUtil.class);
    }
    
    
    /* (non-Javadoc)
     * @see net.jcores.shared.cores.CoreObject#sort()
     */
    @Override
    public TextualRenderElementsUtil sort() {
        return new TextualRenderElementsUtil(sort(new Comparator<TextualRenderElement>() {
            @Override
            public int compare(TextualRenderElement o1, TextualRenderElement o2) {
                return o1.getWordID() - o2.getWordID();
            }
        }).unsafearray());
    }
    
    
    
    /**
     * Returns the bounding box for this set of textual render elements.
     * 
     * @return The box bounding all elements.
     */
    public Rectangle boundingBox() {
        TextualRenderElement first = this.first();
        if(first == null) return null;
        Rectangle rval = first.getGeometry(CoordinatesType.DOCUMENT_BASED);
        
        for (TextualRenderElement r : this) {
            rval = rval.union(r.getGeometry(CoordinatesType.DOCUMENT_BASED));
        }
        
        return rval;
    }
    
    
    
    
    
    /**
     * Searches for the given text in the enclosed {@link TextualRenderElement} objects. If the text 
     * was not found, an empty util is returned. If the text is found multiple times, only the first 
     * occurence is returned. The search is performed in a fuzzy way, ignoring punctuation and 
     * case-sensitivity.<br/><br/>
     * 
     * TODO: Only returns the first occurrence right now. 
     * 
     * @param text The text to search for.
     * @return The first occurence of the text.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TextualRenderElementsUtil search(String text) {
        // Sort our text, otherwise we won't find something sensible
        final TextualRenderElementsUtil util = sort();
        final int size = size();

        // Next, split the input and convert it to lower case (TODO: filter non-words).
        final String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].toLowerCase();
        }

        // Parameters we use for processing
        int start = -1;
        int ptr = 0;
        int miss = 0;
        
        // Go through all elements
        for(int i = 0; i<size; i++) {
            final TextualRenderElement e = util.get(i);
            
            // Check if the curr+++ent word matches the currently searched item
            if(e.getContent().toLowerCase().equals(split[ptr])) {
                ptr++;
                miss = 0;
                if(start < 0) start = i;
            } else {
                miss++;
            }
            
            // When we matched the last element return a result
            if(ptr == split.length) {
                return new TextualRenderElementsUtil(util.slice(start, (i - start) + 1).unsafelist());
            }
            
            // When we had an element and we had too many misses ...
            if(ptr > 0 && miss > 3) {
                i = start + 1;
                start = -1;
                miss = 0;
                ptr = 0;
            }
        }
        
        return new TextualRenderElementsUtil(new ArrayList());
    }
}
