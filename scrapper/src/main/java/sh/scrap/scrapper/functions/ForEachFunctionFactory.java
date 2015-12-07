package sh.scrap.scrapper.functions;

import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;

import javax.inject.Named;

@Named("for-each")
public class ForEachFunctionFactory implements DataScrapperFunctionFactory {

    @Override @SuppressWarnings("unchecked")
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscription -> {
            Object data = context.data();
            if (data instanceof Iterable)
                for (Object entry : (Iterable<Object>) context.data())
                    subscription.onNext(context.withData(entry));
            else if (data instanceof Object[]) {
                Object[] array = (Object[]) data;
                for (int i = 0; i < array.length; i++)
                    subscription.onNext(context.withData(array[i]));
            }
            else
                subscription.onNext(context.withData(data));

            subscription.onComplete();
        };
    }
}
