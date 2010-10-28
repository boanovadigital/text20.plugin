/*
 * PageManagerImpl.java
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
package de.dfki.km.text20.browserplugin.services.pagemanager.impl;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.PluginManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManager;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;
import de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 *
 * @author rb
 *
 */
public class PageManagerImpl implements PageManager {

    /** */
    private final Map<String, RenderElement> id2element = new HashMap<String, RenderElement>();

    /** */
    @SuppressWarnings("unused")
    private final PluginManager pluginManager;

    /** */
    private final Pseudorenderer pseudorenderer;

    /**
     *
     * @param pm
     * @param pseudorenderer
     */
    public PageManagerImpl(final PluginManager pm, final Pseudorenderer pseudorenderer) {
        this.pluginManager = pm;
        this.pseudorenderer = pseudorenderer;
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateBrowserGeometry(final int x, final int y, final int w, final int h) {
        this.pseudorenderer.setGeometry(new Rectangle(x, y, w, h));
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#updateDocumentViewport(int, int)
     */
    @Override
    public void updateDocumentViewport(final int x, final int y) {
        this.pseudorenderer.setViewport(new Point(x, y));
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        if (id == null) return;

        // Special handler
        if (id.equals("#window")) {
            this.pseudorenderer.setStatus(PseudorendererStatus.VISIBLE, value);
        }

        final RenderElement renderElement = this.id2element.get(id);
        if (renderElement == null) return;

        if (flag.equals("REMOVED") && value) {
            this.id2element.remove(id);
            this.pseudorenderer.removeElement(renderElement);
        }

        if (flag.equals("CALLBACK_ENTER_EXIT_GAZE")) {
            renderElement.setMetaAttribute(RenderElementMetaAttribute.CALLBACK_ENTER_EXIT_GAZE, Boolean.valueOf(value));
        }

        if (flag.equals("INVALID")) {
            renderElement.setMetaAttribute(RenderElementMetaAttribute.INVALID, Boolean.TRUE);
        }

        if (flag.equals("FIXED_ON_WINDOW") && value) {
            if (renderElement.getCoordinatesType() == CoordinatesType.VIEWPORT_BASED)
                return;

            // TODO: Basically we assume our coordinates haven't changed much since then.
            final Rectangle geometry = renderElement.getGeometry(CoordinatesType.DOCUMENT_BASED);
            renderElement.setGeometry(geometry, CoordinatesType.VIEWPORT_BASED);
        }
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#updateElementGeometry(java.lang.String, java.lang.String, java.lang.String, int, int, int, int)
     */
    @Override
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final int x, final int y,
                                      final int w, final int h) {

        if (id == null) return;

        RenderElement element = this.id2element.get(id);
        CoordinatesType ct = CoordinatesType.DOCUMENT_BASED;

        if (element == null) {
            Class<? extends RenderElement> rType = RenderElement.class;

            if (type != null) {
                if (type.equals("text")) rType = TextualRenderElement.class;
                if (type.equals("image")) rType = GraphicalRenderElement.class;
            }
            element = this.pseudorenderer.createElement(rType);
        } else {
            // Obtain last coordinates type, in order not to change it in case it has beed switched
            // from DOCUMENT_BASED to WINDOW_BASED
            ct = element.getCoordinatesType();
        }

        // Update the windowGeometry and id
        element.setGeometry(new Rectangle(x, y, w, h), ct);
        element.setIdentifier(id);

        // Try to update content of the element
        if (content != null && element instanceof TextualRenderElement) {
            ((TextualRenderElement) element).setContent(content);
        }

        // When updated, unset invalidity
        element.setMetaAttribute(RenderElementMetaAttribute.INVALID, Boolean.FALSE);

        this.id2element.put(id, element);
    }

    /* (non-Javadoc)
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.PageManager#updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(String id, String key, String value) {
        if (id == null) return;
        if (key == null) return;
        if (value == null) return;

        RenderElement element = this.id2element.get(id);

        // Create element if it's not there yet
        if (element == null) {
            Class<? extends RenderElement> rType = null;

            if (key.equals("textID")) rType = TextualRenderElement.class;
            if (key.equals("wordID")) rType = TextualRenderElement.class;

            if (rType == null)
                throw new IllegalArgumentException("Unable to get infer element type");

            element = this.pseudorenderer.createElement(rType);

            this.id2element.put(id, element);
        }

        // Handle actual parameterts
        if (key.equals("textID")) {
            if (element instanceof TextualRenderElement)
                ((TextualRenderElement) element).setTextID(Integer.parseInt(value));
        }

        if (key.equals("wordID")) {
            if (element instanceof TextualRenderElement)
                ((TextualRenderElement) element).setWordID(Integer.parseInt(value));
        }
    }
}
