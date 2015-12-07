package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class XPathFunctionTest extends FunctionsTestBase {

    @Test
    public void testXPath_xml() throws XPathFactoryConfigurationException, ParserConfigurationException {
        String xml = String.format("<?xml version=\"1.0\" ?><root xmlns=\"%s\" attr=\"1\"><child/></root>",
                XPathFunctionFactory.XHTML_NS);

        testFunction(new XPathFunctionFactory(),
                (NodeList data) -> assertThat(data.item(0).getTextContent(), equalTo("1")),
                xml, "/root/@attr");
    }

    @Test
    public void testXPath_html() throws ParserConfigurationException, XPathFactoryConfigurationException {
        String html = "<html style=\"test\"><body><div /></body></html>";
        testFunction(new XPathFunctionFactory(),
                (NodeList data) -> assertThat(data.item(0).getTextContent(), equalTo("test")),
                html, "/html/@style");
    }
}
