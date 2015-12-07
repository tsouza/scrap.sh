package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;

import javax.inject.Named;
import java.util.Iterator;

@Named("to-object")
public class ToObjectFunctionFactory implements DataScrapperFunctionFactory {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();
                if (data instanceof Iterable) {
                    Iterator<?> iterator = ((Iterable<?>) data).iterator();
                    data = iterator.hasNext() ? null : iterator.next();
                }
                else if (data instanceof Object[]) {
                    Object[] array = (Object[]) data;
                    data = array.length == 0 ? null : array[0];
                }
                else if (data instanceof NodeList) {
                    NodeList list = (NodeList) data;
                    data = list.getLength() == 0 ? null : list.item(0);
                }

                subscription.onNext(context.withData(data));
                subscription.onComplete();
            }

            @Override public void cancel() {}
        });
    }
}
