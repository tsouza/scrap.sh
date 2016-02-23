/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thiago Souza <thiago@scrap.sh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
