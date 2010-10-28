package de.dfki.km.text20.tests;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;

import de.dfki.km.text20.services.pseudorenderer.CoordinatesType;
import de.dfki.km.text20.services.pseudorenderer.RenderElementMetaAttribute;
import de.dfki.km.text20.services.pseudorenderer.renderelements.TextualRenderElement;
import de.dfki.km.text20.services.pseudorenderer.util.TextualRenderElementCharPositions;

/**
 * @author rb
 *
 */
public class JUnitRenderElementCharPositions {

    /**
     * @param name
     * @return .
     */
    public TextualRenderElement createElement(final String name) {
        return new TextualRenderElement() {

            @Override
            public String getContent() {
                return name;
            }

            @Override
            public CoordinatesType getCoordinatesType() {
                return CoordinatesType.DOCUMENT_BASED;
            }

            @Override
            public Rectangle getGeometry(final CoordinatesType type) {
                return new Rectangle(0, 0, 100, 100);
            }

            @Override
            public String getIdentifier() {
                return "id1";
            }

            @Override
            public void setContent(final String content) {
                //
            }

            @Override
            public void setGeometry(final Rectangle rectangle, final CoordinatesType type) {
                //
            }

            @Override
            public void setIdentifier(final String id) {
                //
            }

            @Override
            public int getZIndex() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void setZIndex(int zindex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setMetaAttribute(RenderElementMetaAttribute key, Serializable value) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean isVisible() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void setVisible(boolean visible) {
                // TODO Auto-generated method stub

            }

            @Override
            public Serializable getMetaAttribute(RenderElementMetaAttribute key) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean hasMetaAttribute(RenderElementMetaAttribute key) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int getTextID() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getWordID() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void setTextID(int id) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setWordID(int id) {
                // TODO Auto-generated method stub

            }

        };
    }

    /**
     * @param str
     * @param x
     * @return .
     */
    public int getPos(final String str, final int x) {
        final TextualRenderElement hello = createElement(str);
        final TextualRenderElementCharPositions recp = new TextualRenderElementCharPositions();
        return recp.getPositionOf(hello, CoordinatesType.DOCUMENT_BASED, new Point(x, 0));
    }

    /**
     *
     */
    @Test
    public void testCalcDisplacement() {

        // Test border positions
        Assert.assertTrue(getPos("Hello", -1) == -1);
        Assert.assertTrue(getPos("Hello", 0) == 0);
        Assert.assertTrue(getPos("Hello", 99) == 4);
        Assert.assertTrue(getPos("Hello", 100) == -1);

        // Test balance
        Assert.assertTrue(getPos("MMMM", 49) == 1);
        Assert.assertTrue(getPos("MMMM", 51) == 2);

        // Check imbalance.
        Assert.assertTrue(getPos("llMM", 49) == 2);
        Assert.assertTrue(getPos("MMll", 51) == 1);

    }
}
