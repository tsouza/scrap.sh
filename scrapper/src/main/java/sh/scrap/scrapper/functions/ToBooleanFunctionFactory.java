package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import java.util.Map;

@Name("to-boolean")
public class ToBooleanFunctionFactory implements DataScrapperFunctionFactory<Void> {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                String data = context.data().toString().trim().toLowerCase();
                data = data.matches("^-?[0-9\\.]+$") ? "1" : data;
                switch (data) {
                    case "0": case "false": case "no":
                        subscription.onNext(context.withData(false));
                        break;
                    default:
                        subscription.onNext(context.withData(true));
                }
                subscription.onComplete();
            }
            @Override public void cancel() {}
        });
    }
}
