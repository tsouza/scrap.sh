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
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.core.ReactorDataScrapperBuilder;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Map;

public class FunctionsTestBase extends ReactorDataScrapperBuilder {

    @SuppressWarnings("unchecked")
    protected <O> void testFunction(DataScrapperFunctionFactory factory, Verifier<O> verifier, Object input,
                                    Object mainArgument, Map<String, Object> annotations) {
        factory.create(getName(factory), getLibrary(), mainArgument, annotations).
                process(context(null, null, input)).
                subscribe(new Subscriber<DataScrapperExecutionContext>() {
                    @Override
                    public void onNext(DataScrapperExecutionContext dataScrapperExecutionContext) {
                        verifier.verify((O) dataScrapperExecutionContext.data());
                    }

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof RuntimeException)
                            throw (RuntimeException) t;
                        else throw new RuntimeException(t);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    protected <O> void testFunction(DataScrapperFunctionFactory factory, Verifier<O> verifier, Object input,
                                    Object mainArgument) {
        testFunction(factory, verifier, input, mainArgument, Collections.emptyMap());
    }

    protected <O> void testFunction(DataScrapperFunctionFactory factory, Verifier<O> verifier, Object input) {
        testFunction(factory, verifier, input, null, Collections.emptyMap());
    }

    protected String getName(DataScrapperFunctionFactory factory) {
        return factory.getClass().getAnnotation(Name.class).value();
    }

    protected NodeList nodeList() {
        try {
            String xml = "<root><entry>1</entry><entry>2</entry><entry>3</entry></root>";
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes())).
                    getElementsByTagName("entry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected interface Verifier<O> {
        void verify(O data);
    }
}
