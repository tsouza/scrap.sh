package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

@Name("lib")
public class LibFunctionFactory implements DataScrapperFunctionFactory {
    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data;
                try {
                    data = library.invoke(name, context.data(), args);
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
