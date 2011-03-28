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
import net.xeoh.plugins.diagnosis.local.Diagnosis;
import net.xeoh.plugins.diagnosis.local.DiagnosisChannel;
import net.xeoh.plugins.diagnosis.local.options.status.OptionInfo;
import de.dfki.km.text20.browserplugin.services.pagemanager.PageManager;
import de.dfki.km.text20.browserplugin.services.pagemanager.diagnosis.channels.tracing.PageManagerTracer;
import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.Pseudorenderer;
import de.dfki.km.text20.services.pseudorenderer.PseudorendererStatus;
import de.dfki.km.text20.services.pseudorenderer.RenderElement;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;
import de.dfki.km.text20.services.pseudorenderer.renderelements.GraphicalRenderElement;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;

/**
 * @author Ralf Biedert
 */
public class PageManagerImpl implements PageManager {

    /** In case we need a plugin */
    private final PluginManager pluginManager;

    /** Maps IDs to render elements */
    private final Map<String, RenderElement> id2element = new HashMap<String, RenderElement>();

    /** Needed to register and retrieve elements */
    private final Pseudorenderer pseudorenderer;

    /** Responsible for tracing messages */
    private final DiagnosisChannel<String> diagnosis;

    /**
     *
     * @param pluginManager
     * @param pseudorenderer
     */
    public PageManagerImpl(final PluginManager pluginManager, final Pseudorenderer pseudorenderer) {
        this.pluginManager = pluginManager;
        this.pseudorenderer = pseudorenderer;

        this.diagnosis = this.pluginManager.getPlugin(Diagnosis.class).channel(PageManagerTracer.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#
     * updateBrowserGeometry(int, int, int, int)
     */
    @Override
    public void updateBrowserGeometry(final int x, final int y, final int w, final int h) {
        final Rectangle r = new Rectangle(x, y, w, h);

        this.diagnosis.status("updateBrowserGeometry/call", new OptionInfo("rectangle", r));

        this.pseudorenderer.setGeometry(r);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#
     * updateDocumentViewport(int, int)
     */
    @Override
    public void updateDocumentViewport(final int x, final int y) {
        final Point p = new Point(x, y);

        this.diagnosis.status("updateDocumentViewport/call", new OptionInfo("point", p));

        this.pseudorenderer.setViewport(p);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#
     * updateElementFlag(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void updateElementFlag(final String id, final String flag, final boolean value) {
        this.diagnosis.status("updateElementFlag/start", new OptionInfo("id", id), new OptionInfo("flag", flag), new OptionInfo("value", Boolean.valueOf(value)));

        if (id == null) {
            this.diagnosis.status("updateElementFlag/end/id/unusual", new OptionInfo("id", id));
            return;
        }

        // Special handler
        if (id.equals("#window")) {
            this.pseudorenderer.setStatus(PseudorendererStatus.VISIBLE, value);
        }

        final RenderElement renderElement = this.id2element.get(id);
        if (renderElement == null) {
            this.diagnosis.status("updateElementFlag/end/renderelement/unusual");
            return;
        }

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
            if (renderElement.getCoordinatesType() == CoordinatesType.VIEWPORT_BASED) {
                this.diagnosis.status("updateElementFlag/end/coordinatestype/viewportbased");
                return;
            }

            // TODO: Basically we assume our coordinates haven't changed much since then.
            final Rectangle geometry = renderElement.getGeometry(CoordinatesType.DOCUMENT_BASED);
            renderElement.setGeometry(geometry, CoordinatesType.VIEWPORT_BASED);
        }

        this.diagnosis.status("updateElementFlag/end");
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.impl.PageManager#
     * updateElementGeometry(java.lang.String, java.lang.String, java.lang.String, int,
     * int, int, int)
     */
    @Override
    public void updateElementGeometry(final String id, final String type,
                                      final String content, final int x, final int y,
                                      final int w, final int h) {
        this.diagnosis.status("updateElementGeometry/start");

        // If we have no ID, do nothing.
        if (id == null) {
            this.diagnosis.status("updateElementGeometry/end/id/unusual", new OptionInfo("id", id));
            return;
        }

        // Get the element, if we already have it.
        RenderElement element = this.id2element.get(id);
        CoordinatesType ct = CoordinatesType.DOCUMENT_BASED;

        // In case we don't have the element, we need to create it
        if (element == null) {
            Class<? extends RenderElement> rType = RenderElement.class;

            // Check what type of element we need to create
            if (type != null) {
                if (type.equals("text")) rType = TextualRenderElement.class;
                if (type.equals("image")) rType = GraphicalRenderElement.class;
            }

            // And create it!
            element = this.pseudorenderer.createElement(rType);
        } else {
            // Obtain last coordinates type, in order not to change it in case it has beed
            // switched from DOCUMENT_BASED to WINDOW_BASED
            ct = element.getCoordinatesType();
        }

        // Now we have an element, either a new one, or a preexisting one, update the
        // windowGeometry and id
        element.setGeometry(new Rectangle(x, y, w, h), ct);
        element.setIdentifier(id);

        // Try to update content of the element
        if (content != null) {
            if (element instanceof TextualRenderElement)
                ((TextualRenderElement) element).setContent(content);

            if (element instanceof GraphicalRenderElement)
                ((GraphicalRenderElement) element).setSource(content);
        }

        // When updated, unset invalidity
        element.setMetaAttribute(RenderElementMetaAttribute.INVALID, Boolean.FALSE);

        // Remember the element's id.
        this.id2element.put(id, element);

        this.diagnosis.status("updateElementGeometry/end");
    }

    /*
     * (non-Javadoc)
     *
     * @see de.dfki.km.augmentedtext.browserplugin.services.pagemanager.PageManager#
     * updateElementMetaInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void updateElementMetaInformation(String id, String key, String value) {
        this.diagnosis.status("updateElementMetaInformation/start", new OptionInfo("id", id), new OptionInfo("key", key), new OptionInfo("value", value));
        if (id == null || key == null || value == null) {
            this.diagnosis.status("updateElementMetaInformation/end/unusual");
            return;
        }

        RenderElement element = this.id2element.get(id);

        // Create element if it's not there yet
        if (element == null) {
            Class<? extends RenderElement> rType = null;

            if (key.equals("textID")) rType = TextualRenderElement.class;
            if (key.equals("wordID")) rType = TextualRenderElement.class;

            if (rType == null) {
                this.diagnosis.status("updateElementMetaInformation/exception", new OptionInfo("message", "Unable to get infer element type"));

                throw new IllegalArgumentException("Unable to get infer element type");
            }

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

        this.diagnosis.status("updateElementMetaInformation/end");
    }
}
