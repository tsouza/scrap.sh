package sh.scrap.scrapper.functions;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;

import javax.inject.Named;
import java.util.Iterator;

@Named("to-iterable")
public class ToIterableFunctionFactory implements DataScrapperFunctionFactory {


    @Override
    @SuppressWarnings("unchecked")
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscription -> {
            Object data = context.data();
            if (data.getClass().isArray())
                data = toIterable((Object[]) data);
            else if (data instanceof NodeList)
                data = toIterable((NodeList) data);
            else if (!(data instanceof Iterable))
                data = toIterable(new Object[] { data });

            subscription.onNext(context.withData(data));
            subscription.onComplete();
        };
    }

    private Iterable<Object> toIterable(Object[] array) {
        return () -> new Iterator<Object>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Object next() {
                return array[index++];
            }
        };
    }

    private Iterable<Node> toIterable(NodeList list) {
        return () -> new Iterator<Node>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < list.getLength();
            }

            @Override
            public Node next() {
                return list.item(index++);
            }
        };
    }
}
