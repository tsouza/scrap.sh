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

import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.xpath.XPathEvaluator;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.Map;

import static sh.scrap.scrapper.functions.ToXmlFunctionFactory.toXML;

@Name("xpath")
public class XPathFunctionFactory implements DataScrapperFunctionFactory<String> {

    public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    static {
        System.setProperty("javax.xml.xpath.XPathFactory:" + NamespaceConstant.OBJECT_MODEL_SAXON,
                "net.sf.saxon.xpath.XPathFactoryImpl");
    }

    private final XPath xpath;

    public XPathFunctionFactory() throws XPathFactoryConfigurationException, ParserConfigurationException {
        XPathFactory factory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
        xpath = factory.newXPath();
        ((XPathEvaluator) xpath).getStaticContext().setDefaultElementNamespace(XHTML_NS);
    }

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       String mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> {
            try {
                XPathExpression xpath = this.xpath.compile(mainArgument);
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        Object data = context.data();
                        if (data instanceof Node)
                            process(xpath, context, subscriber);

                        else try {
                            context.objectProcessed(data);
                            process(xpath, context.withData(toXML(data)), subscriber);
                        } catch (SAXException | IOException | ParserConfigurationException e) {
                            subscriber.onError(e);
                            subscriber.onComplete();
                        }


                    }
                    @Override public void cancel() {}
                });
            } catch (XPathExpressionException e) {
                subscriber.onError(e);
            }
        };
    }

    private void process(XPathExpression xpath, DataScrapperExecutionContext context, Subscriber<? super DataScrapperExecutionContext> subscriber) {
        try {
            Object data = context.data();
            context.objectProcessed(data);
            Object result = xpath.evaluate(data, XPathConstants.NODESET);
            subscriber.onNext(context.withData(result));
        } catch (XPathExpressionException e) {
            subscriber.onError(e);
            return;
        }
        subscriber.onComplete();
    }

}
