package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CssFunctionTest extends FunctionsTestBase {

    @Test
    public void testCss_simple() throws ParserConfigurationException {
        String html = "<html><body><div class=\"test\"></div></body></html>";
        testFunction(new CssFunctionFactory(),
                (List<Element> data) ->
                        assertThat(data.get(0).getAttribute("class"), equalTo("test")),
                html, "div.test");
    }

}
