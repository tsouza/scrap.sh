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
public class JsonFunctionFactory extends ToStringFunctionFactory implements DataScrapperFunctionFactory {

    private final JsonProvider jsonProvider = Configuration
            .defaultConfiguration()
            .jsonProvider(createProvider())
            .jsonProvider();

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        JsonPath path = JsonPath.compile((String) args[0]);
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();

                if (data instanceof List || data instanceof Map)
                    process(path, context, subscriber);

                else if (data instanceof String) {
                    try {
                        data = jsonProvider.parse((String) data);
                    } catch (Exception e) {
                        subscriber.onError(e);
                        return;
                    }
                    process(path, context.withData(data), subscriber);

                } else try {
                    process(path, context.withData(JsonFunctionFactory.this.toString(data)), subscriber);
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

    private JsonProvider createProvider() {
        JacksonJsonProvider provider = new JacksonJsonProvider();
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        provider.getObjectMapper().configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        return provider;
    }
}
