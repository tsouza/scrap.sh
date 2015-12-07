package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ToXmlFunctionTest extends FunctionsTestBase {

    @Test
    public void testToXml_xml() throws ParserConfigurationException {
        String xml = "<?xml version=\"1.0\" ?><root attr=\"1\"><child/></root>";
        testFunction(new ToXmlFunctionFactory(),
                (Element data) ->
                        assertThat(data.getAttribute("attr"), equalTo("1")),
                xml);
    }

    @Test
    public void testToXml_html() throws ParserConfigurationException {
        String xml = "<html style=\"test\"><body><div /></body></html>";
        testFunction(new ToXmlFunctionFactory(),
                (Element data) ->
                        assertThat(data.getAttribute("style"), equalTo("test")),
                xml);
    }
}
