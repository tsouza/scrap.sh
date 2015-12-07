package sh.scrap.scrapper;

import org.reactivestreams.Publisher;
import reactor.Environment;
import reactor.rx.Stream;

import javax.inject.Named;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

import static reactor.rx.Streams.create;

public class DataScrapperBuilder {

    static { Environment.initialize(); }

    private static final Map<String, DataScrapperFunctionFactory> factories = lookupFactories();

    private final JSLibrary library = new JSLibrary();

    private Map<String, List<DataScrapperFunction>> fieldFunctions =
            new HashMap<>();

    public DataScrapperBuilder map(String fieldName, String functionName, Object... args) {
        return map(fieldName, functionOf("to-object")).map(fieldName, functionOf(functionName, args));
    }

    public DataScrapperBuilder forEach(String fieldName) {
        return map(fieldName, functionOf("to-iterable")).map(fieldName, functionOf("for-each"));
    }

    public void addLibrarySource(String source) throws ScriptException {
        library.engine.eval(source);
    }

    public DataScrapper build() {
        return (metadata, data) -> create(build(metadata, data))
                .buffer(fieldFunctions.size())
                .map(results -> {
                    Map<String, Object> output = new HashMap<>();
                    results.forEach(context -> output.put(context.fieldName(), context.data()));
                    return output;
                })
                .dispatchOn(Environment.get())
                .next();
    }

    private Publisher<DataScrapperExecutionContext> build(Map<String, Object> metadata, Object data) {
        return subscriber -> fieldFunctions.entrySet().stream()
                .forEach(entry -> build(entry.getValue(), context(entry.getKey(), metadata, data))
                        .dispatchOn(Environment.get())
                        .consume(subscriber::onNext));
    }

    private Stream<DataScrapperExecutionContext> build(List<DataScrapperFunction> functions, DataScrapperExecutionContext context) {
        Stream<DataScrapperExecutionContext> stream = create(subscriber -> {
            subscriber.onNext(context);
            subscriber.onComplete();
        });
        for (DataScrapperFunction function : functions)
            stream = stream.map(function::process).merge();
        return stream;
    }

    private DataScrapperBuilder map(String fieldName, DataScrapperFunction function) {
        List<DataScrapperFunction> fieldFunctions = this.fieldFunctions.get(fieldName);
        if (fieldFunctions == null) {
            fieldFunctions = new ArrayList<>();
            this.fieldFunctions.put(fieldName, fieldFunctions);
        }
        fieldFunctions.add(function);
        return this;
    }

    private DataScrapperFunction functionOf(String name, Object... args) {
        String[] n = name.split(":");
        if (n.length == 1)
            return factories.get(n[0])
                    .create(n[0], library, args);
        return factories.get(n[0])
                .create(n[1], library, args);
    }

    private DataScrapperFunction functionOf(String namespace, String name, Object... args) {
        DataScrapperFunctionFactory factory = factories.get(namespace);
        DataScrapperFunction function = factory.create(name, library, args);
        return function;
    }

    private static Map<String, DataScrapperFunctionFactory> lookupFactories() {

        ServiceLoader<DataScrapperFunctionFactory> loader = ServiceLoader.load(DataScrapperFunctionFactory.class);

        Map<String, DataScrapperFunctionFactory> factories = new HashMap<>();

        for (DataScrapperFunctionFactory factory : loader)
            factories.put(factory.getClass().getAnnotation(Named.class).value(),
                    new NullWrapper(factory));

        return factories;
    }

    private static class JSLibrary implements DataScrapperFunctionLibrary {

        final ScriptEngine engine = new ScriptEngineManager().
                getEngineByName("JavaScript");

        @Override
        public Object invoke(String functionName, Object data) throws Exception {
            Invocable inv = (Invocable) engine;
            return inv.invokeFunction(functionName, data);
        }
    }

    private static class NullWrapper implements DataScrapperFunctionFactory {

        private final DataScrapperFunctionFactory wrapped;

        private NullWrapper(DataScrapperFunctionFactory wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
            DataScrapperFunction function = wrapped.create(name, library, args);
            return (context) -> context.data() == null ?
                    publisher -> {
                        publisher.onNext(context);
                        publisher.onComplete();
                    } : function.process(context);
        }
    }

    protected DataScrapperExecutionContext context(String fieldName, Map<String, Object> metadata, Object data) {
        return new DataScrapperExecutionContext() {

            @Override
            public String fieldName() {
                return fieldName;
            }

            @Override
            public Map<String, Object> metadata() {
                return metadata;
            }

            @Override
            public Object data() {
                return data;
            }

            @Override
            public DataScrapperExecutionContext withData(Object data) {
                return context(fieldName, metadata, data);
            }

            @Override
            public Publisher<DataScrapperExecutionContext> invoke(String functionName, Object... args) {
                return factories.get(functionName).create(functionName, library, args).
                        process(this);
            }

        };
    }
}



