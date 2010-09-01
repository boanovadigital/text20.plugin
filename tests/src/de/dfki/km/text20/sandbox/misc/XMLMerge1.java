package de.dfki.km.text20.sandbox.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author rb
 *
 */
public class XMLMerge1 {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        // proper error/exception handling omitted for brevity
        final File file1 = new File("1.xml");
        final File file2 = new File("2.xml");
        final Document doc = merge("/sessionRecord", file1, file2);

        final FileOutputStream fos = new FileOutputStream(new File("res.xml"));
        print(doc, fos);
    }

    private static Document merge(final String expression, final File... files)
                                                                               throws Exception {
        final XPathFactory xPathFactory = XPathFactory.newInstance();
        final XPath xpath = xPathFactory.newXPath();
        final XPathExpression compiledExpression = xpath.compile(expression);
        return merge(compiledExpression, files);
    }

    private static Document merge(final XPathExpression expression, final File... files)
                                                                                        throws Exception {
        final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringElementContentWhitespace(true);
        final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        final Document base = docBuilder.parse(files[0]);

        final Node results = (Node) expression.evaluate(base, XPathConstants.NODE);
        if (results == null)
            throw new IOException(files[0] + ": expression does not evaluate to node");

        for (int i = 1; i < files.length; i++) {
            final Document merge = docBuilder.parse(files[i]);
            final Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
            while (nextResults.hasChildNodes()) {
                Node kid = nextResults.getFirstChild();
                nextResults.removeChild(kid);
                kid = base.importNode(kid, true);
                results.appendChild(kid);
            }
        }

        return base;
    }

    private static void print(final Document doc, final OutputStream os) throws Exception {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        final DOMSource source = new DOMSource(doc);
        final Result result = new StreamResult(os);
        transformer.transform(source, result);
    }

}