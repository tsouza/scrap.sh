package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import java.util.Map;

@Name("lib")
public class LibFunctionFactory implements DataScrapperFunctionFactory<Object> {
    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object mainArgument, Map<String, Object> annotations) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data;
                try {
                    data = library.invoke(name, context.data(), mainArgument, annotations);
                } catch (Exception e) {
                    subscriber.onError(e);
                    subscriber.onComplete();
                    return;
                }
                subscriber.onNext(context.withData(data));
                subscriber.onComplete();
            }

            @Override
            public void cancel() {}
        });
    }
}
