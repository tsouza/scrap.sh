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

import nu.validator.htmlparser.common.Heuristics;
import nu.validator.htmlparser.dom.HtmlDocumentBuilder;
import org.reactivestreams.Subscription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

@Name("to-xml")
public class ToXmlFunctionFactory implements DataScrapperFunctionFactory<Void> {

    private static final Factory xmlParser;
    private static final Factory htmlParser;

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    Object data = context.data();

                    if (!(data instanceof Node)) {
                        context.objectProcessed(data);
                        data = toXML(data);
                    }

                    subscriber.onNext(context.withData(data));

                } catch (IOException | SAXException | ParserConfigurationException e) {
                    subscriber.onError(e);
                    return;
                }
                subscriber.onComplete();
            }

            @Override public void cancel() {}
        });
    }

    static Node toXML(Object data) throws IOException, SAXException, ParserConfigurationException {
        String body = data.toString().trim();

        Parser parser = body.startsWith("<?xml") ?
                xmlParser.create() : htmlParser.create();

        return parser.parse(new InputSource(new StringReader(body)))
                .getDocumentElement();
    }

    interface Parser {
        Document parse(InputSource source) throws IOException, SAXException;
    }

    static {
        try {
            DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlParser = () -> DocumentBuilderFactory.newInstance().newDocumentBuilder()::parse;

            htmlParser = () -> {
                HtmlDocumentBuilder htmlBuilder = new HtmlDocumentBuilder();
                htmlBuilder.setHeuristics(Heuristics.ALL);
                return htmlBuilder::parse;
            };


        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }

    }

    interface Factory {
        Parser create() throws ParserConfigurationException;
    }
}
