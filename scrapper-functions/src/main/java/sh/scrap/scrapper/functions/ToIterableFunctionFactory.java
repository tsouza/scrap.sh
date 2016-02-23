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

import org.reactivestreams.Subscription;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import java.util.Iterator;
import java.util.Map;

@Name("to-iterable")
public class ToIterableFunctionFactory implements DataScrapperFunctionFactory<Void> {

    @Override @SuppressWarnings("unchecked")
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();
                if (data.getClass().isArray())
                    data = toIterable((Object[]) data);
                else if (data instanceof NodeList)
                    data = toIterable((NodeList) data);
                else if (!(data instanceof Iterable))
                    data = toIterable(new Object[] { data });

                subscription.onNext(context.withData(data));
                subscription.onComplete();
            }

            @Override public void cancel() {}
        });
    }

    private Iterable<Object> toIterable(Object[] array) {
        return () -> new Iterator<Object>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Object next() {
                return array[index++];
            }
        };
    }

    private Iterable<Node> toIterable(NodeList list) {
        return () -> new Iterator<Node>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < list.getLength();
            }

            @Override
            public Node next() {
                return list.item(index++);
            }
        };
    }
}
