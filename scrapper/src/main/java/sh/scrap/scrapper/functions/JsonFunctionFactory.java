package sh.scrap.scrapper.functions;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.rx.Streams;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;

import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named("json")
public class JsonFunctionFactory implements DataScrapperFunctionFactory {

    private final JsonProvider jsonProvider = Configuration
            .defaultConfiguration()
            .jsonProvider(new JacksonJsonProvider())
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

                } else
                    Streams.create(context.invoke("to-string")).
                            when(Throwable.class, subscriber::onError).
                            consume(toString -> {
                                process(path, toString, subscriber);
                            });
            }

            @Override public void cancel() {}
        });
    }

    private void process(JsonPath path, DataScrapperExecutionContext context,
                         Subscriber<? super DataScrapperExecutionContext> subscriber) {
        Object data = context.data();
        try {
            data = path.read(data);
        } catch (Exception e) {
            subscriber.onError(e);
            subscriber.onComplete();
            return;
        }
        subscriber.onNext(context.withData(data));
        subscriber.onComplete();
    }
}
