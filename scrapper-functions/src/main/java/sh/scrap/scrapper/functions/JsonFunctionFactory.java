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

import com.fasterxml.jackson.core.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import javax.xml.transform.TransformerException;
import java.util.List;
import java.util.Map;

@Name("json")
public class JsonFunctionFactory implements DataScrapperFunctionFactory<String> {

    public static final JsonProvider jsonProvider = Configuration
            .defaultConfiguration()
            .jsonProvider(createProvider())
            .jsonProvider();

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       String mainArgument, Map<String, Object> annotations) {
        JsonPath path = JsonPath.compile(mainArgument);
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();

                if (data instanceof List || data instanceof Map)
                    process(path, context, subscriber);

                else if (data instanceof String) {
                    try {
                        context.objectProcessed(data);
                        data = jsonProvider.parse((String) data);
                    } catch (Exception e) {
                        subscriber.onError(e);
                        return;
                    }
                    process(path, context.withData(data), subscriber);

                } else try {
                    context.objectProcessed(data);
                    process(path, context.withData(ToStringFunctionFactory.toString(data)), subscriber);
                } catch (TransformerException e) {
                    subscriber.onError(e);
                    subscriber.onComplete();
                }
            }

            @Override public void cancel() {}
        });
    }

    private void process(JsonPath path, DataScrapperExecutionContext context,
                         Subscriber<? super DataScrapperExecutionContext> subscriber) {
        Object data = context.data();
        try {
            context.objectProcessed(data);
            data = path.read(data);
        } catch (PathNotFoundException e) {
            data = null;
        } catch (Exception e) {
            subscriber.onError(e);
            subscriber.onComplete();
            return;
        }
        subscriber.onNext(context.withData(data));
        subscriber.onComplete();
    }

    private static JsonProvider createProvider() {
        JacksonJsonProvider provider = new JacksonJsonProvider();
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return provider;
    }
}
