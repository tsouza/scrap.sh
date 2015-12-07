package sh.scrap.scrapper.functions;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperBuilder;
import sh.scrap.scrapper.DataScrapperExecutionContext;
import sh.scrap.scrapper.DataScrapperFunctionFactory;

import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FunctionsTest extends DataScrapperBuilder {

    private int forEachCounter;

    @Test
    public void testForEach_list() {
        forEachCounter = 0;
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        testFunction(new ForEachFunctionFactory(),
                data -> forEachCounter++,
                list);

        assertThat(forEachCounter, equalTo(list.size()));
    }

    @Test
    public void testToIterable_singleObject() {
        String test = "test";
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data.iterator().next(), equalTo(test)),
                test);
    }

    @Test
    public void testToIterable_list() {
        List<String> test = Arrays.asList("test");
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data, equalTo(test)),
                test);
    }

    @Test
    public void testToIterable_array() {
        String[] test = new String[] { "test" };
        testFunction(new ToIterableFunctionFactory(),
                (Iterable<String> data) ->
                        assertThat(data.iterator().next(), equalTo(test[0])),
                test);
    }

    private <O> void testFunction(DataScrapperFunctionFactory factory, Verifier<O> verifier, Object input, Object... args) {
        factory.create(getName(factory), null, args).
                process(context(null, null, input)).
                subscribe(new Subscriber<DataScrapperExecutionContext>() {
                    @Override
                    public void onNext(DataScrapperExecutionContext dataScrapperExecutionContext) {
                        verifier.verify((O) dataScrapperExecutionContext.data());
                    }
                    @Override public void onSubscribe(Subscription s) {}
                    @Override public void onError(Throwable t) {}
                    @Override public void onComplete() {}
                });
    }

    private String getName(DataScrapperFunctionFactory factory) {
        return factory.getClass().getAnnotation(Named.class).value();
    }

    interface Verifier<O> {
        void verify(O data);
    }
}
