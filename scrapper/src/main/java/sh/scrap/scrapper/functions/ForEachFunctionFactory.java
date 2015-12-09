package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import java.util.Map;

@Name("for-each")
public class ForEachFunctionFactory implements DataScrapperFunctionFactory<Void> {

    @Override @SuppressWarnings("unchecked")
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Void mainArgument, Map<String, Object> annotations) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();
                if (data instanceof Iterable)
                    for (Object entry : (Iterable<Object>) context.data())
                        subscription.onNext(context.withData(entry));
                else if (data instanceof Object[]) {
                    Object[] array = (Object[]) data;
                    for (Object anArray : array)
                        subscription.onNext(context.withData(anArray));
                } else if (data instanceof NodeList) {
                    NodeList list = (NodeList) data;
                    for (int i = 0; i < list.getLength(); i++)
                        subscription.onNext(context.withData(list.item(i)));
                }
                else
                    subscription.onNext(context.withData(data));

                subscription.onComplete();
            }

            @Override public void cancel() {}
        });

    }
}
