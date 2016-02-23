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

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;
import se.fishtank.css.selectors.selector.Selector;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Name("css")
public class CssFunctionFactory implements DataScrapperFunctionFactory<String> {

    public CssFunctionFactory() throws ParserConfigurationException {}

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       String mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> {
            List<Selector> selectors = Selectors.parse(mainArgument);
            subscriber.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    Object data = context.data();
                    if (data instanceof Node)
                        process(selectors, context, subscriber);
                    else try {
                        context.objectProcessed(data);
                        data = ToXmlFunctionFactory.toXML(data);
                        process(selectors, context.withData(data), subscriber);
                    } catch (IOException | SAXException | ParserConfigurationException e) {
                        subscriber.onError(e);
                        subscriber.onComplete();
                    }
                }

                @Override public void cancel() {}
            });

        };
    }

    private void process(List<Selector> selectors, DataScrapperExecutionContext context,
                         Subscriber<? super DataScrapperExecutionContext> subscriber) {
        Object data;
        try {
            context.objectProcessed(context.data());

            Node node = (Node) context.data();
            Document document = node.getOwnerDocument();
            data = new Selectors<>(new W3CNode(document))
                    .querySelectorAll(selectors);
        } catch (Exception e) {
            subscriber.onError(e);
            subscriber.onComplete();
            return;
        }

        subscriber.onNext(context.withData(data));
        subscriber.onComplete();
    }
}
