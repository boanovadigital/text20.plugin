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

            public String getContent() {
                return name;
            }

            public CoordinatesType getCoordinatesType() {
                return CoordinatesType.DOCUMENT_BASED;
            }

            public Rectangle getGeometry(final CoordinatesType type) {
                return new Rectangle(0, 0, 100, 100);
            }

            public String getIdentifier() {
                return "id1";
            }

            public void setContent(final String content) {
                //
            }

            public void setGeometry(final Rectangle rectangle, final CoordinatesType type) {
                //                
            }

            public void setIdentifier(final String id) {
                //
            }

            public int getZIndex() {
                // TODO Auto-generated method stub
                return 0;
            }

            public void setZIndex(int zindex) {
                // TODO Auto-generated method stub

            }

            public void setMetaAttribute(RenderElementMetaAttribute key,
                                         Serializable value) {
                // TODO Auto-generated method stub

            }

            public boolean isVisible() {
                // TODO Auto-generated method stub
                return false;
            }

            public void setVisible(boolean visible) {
                // TODO Auto-generated method stub

            }

            public Serializable getMetaAttribute(RenderElementMetaAttribute key) {
                // TODO Auto-generated method stub
                return null;
            }

            public boolean hasMetaAttribute(RenderElementMetaAttribute key) {
                // TODO Auto-generated method stub
                return false;
            }

            public int getTextID() {
                // TODO Auto-generated method stub
                return 0;
            }

            public int getWordID() {
                // TODO Auto-generated method stub
                return 0;
            }

            public void setTextID(int id) {
                // TODO Auto-generated method stub

            }

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
