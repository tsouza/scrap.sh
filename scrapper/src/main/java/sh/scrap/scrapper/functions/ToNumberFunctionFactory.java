package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

@Name("to-number")
public class ToNumberFunctionFactory implements DataScrapperFunctionFactory {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                String data = context.data().toString();
                try {
                    subscription.onNext(context.withData(Integer.parseInt(data)));
                    subscription.onComplete();
                } catch (NumberFormatException e) {
                    try {
                        subscription.onNext(context.withData(Double.parseDouble(data)));
                        subscription.onComplete();
                    } catch (NumberFormatException ex) {
                        subscription.onError(e);
                        subscription.onComplete();
                    }
                }
            }
            @Override public void cancel() {}
        });
    }
}
