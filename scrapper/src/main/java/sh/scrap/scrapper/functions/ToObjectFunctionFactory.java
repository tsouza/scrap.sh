package sh.scrap.scrapper.functions;

import org.reactivestreams.Subscription;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;

import java.util.Iterator;
import java.util.Map;

@Name("to-object")
public class ToObjectFunctionFactory implements DataScrapperFunctionFactory<Void> {

    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Void mainArgument, Map<String, Object> annotations) {
        return context -> subscription -> subscription.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                Object data = context.data();
                if (data instanceof Iterable) {
                    Iterator<?> iterator = ((Iterable<?>) data).iterator();
                    try {
                        data = iterator.next();
                    } catch (Exception e) {
                        data = null;
                    }
                }
                else if (data instanceof Object[]) {
                    Object[] array = (Object[]) data;
                    data = array.length == 0 ? null : array[0];
                }
                else if (data instanceof NodeList && !(data instanceof Node)) {
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
