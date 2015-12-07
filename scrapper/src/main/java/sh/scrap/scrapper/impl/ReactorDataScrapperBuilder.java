package sh.scrap.scrapper.impl;

import org.reactivestreams.Publisher;
import reactor.Environment;
import reactor.rx.Stream;
import sh.scrap.scrapper.*;

import javax.inject.Named;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;
import java.util.stream.Collectors;

import static reactor.rx.Streams.create;

public class ReactorDataScrapperBuilder implements DataScrapperBuilder {

    static { Environment.initialize(); }

    private static final Map<String, DataScrapperFunctionFactory> factories = lookupFactories();

    private final JSLibrary library;
    private final Map<String, List<Step>> fieldSteps = new HashMap<>();

    public ReactorDataScrapperBuilder() {
        library = new JSLibrary();
    }

    @Override
    public Field field(String fieldName) {
        List<Step> steps = new ArrayList<>();
        steps.add(new FunctionStep(createFunction("to-object")));
        fieldSteps.put(fieldName, steps);
        return new FieldBuilder(fieldName, steps);
    }

    @Override
    public DataScrapper build() {
        return (metadata, data) -> create(build(metadata, data))
                .buffer(fieldSteps.size())
                .map(results -> {
                    Map<String, Object> output = new HashMap<>();
                    results.forEach(context -> output.put(context.fieldName(), context.data()));
                    return output;
                })
                .dispatchOn(Environment.get())
                .next();
    }

    private Publisher<DataScrapperExecutionContext> build(Map<String, Object> metadata, Object data) {
        return subscriber -> fieldSteps.entrySet().stream()
                .forEach(entry -> build(entry.getValue(), context(entry.getKey(), metadata, data))
                        .dispatchOn(Environment.get())
                        .observeError(Throwable.class, (o, e) -> subscriber.onError(e))
                        .observeComplete(v -> subscriber.onComplete())
                        .consume(subscriber::onNext));
    }

    private Stream<DataScrapperExecutionContext> build(List<Step> steps, DataScrapperExecutionContext context) {
        Stream<DataScrapperExecutionContext> stream = create(subscriber -> {
            subscriber.onNext(context);
            subscriber.onComplete();
        });
        for (Step step : steps)
            stream = step.build(stream);
        return stream;
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

    private DataScrapperFunction createFunction(String name, Object... args) {
        String[] n = name.split(":");
        if (n.length == 1)
            return factories.get(n[0])
                    .create(n[0], library, args);
        return factories.get(n[0])
                .create(n[1], library, args);
    }

    class FieldBuilder implements Field {

        private final String fieldName;
        private final List<Step> steps;

        FieldBuilder(String fieldName, List<Step> steps) {
            this.fieldName = fieldName;
            this.steps = steps;
        }

        @Override
        public Field map(String functionName, Object... args) {
            steps.add(new FunctionStep(createFunction(functionName, args)));
            return this;
        }

        @Override
        public Field forEach() {
            map("to-iterable");
            map("for-each");
            CompositeStep step = new CompositeStep(fieldName);
            steps.add(step);
            return new FieldBuilder(fieldName, step.children);
        }

        @Override
        public DataScrapper build() {
            return ReactorDataScrapperBuilder.this.build();
        }

    }

    interface Step {
        Stream<DataScrapperExecutionContext> build(Stream<DataScrapperExecutionContext> stream);
    }

    class FunctionStep implements Step {

        final DataScrapperFunction function;

        FunctionStep(DataScrapperFunction function) {
            this.function = function;
        }

        @Override
        public Stream<DataScrapperExecutionContext> build(Stream<DataScrapperExecutionContext> stream) {
            return stream.map(function::process).merge();
        }
    }

    class CompositeStep implements Step {

        final String fieldName;
        final List<Step> children = new ArrayList<>();

        CompositeStep(String fieldName) {
            this.fieldName = fieldName;
        }

        @Override
        public Stream<DataScrapperExecutionContext> build(Stream<DataScrapperExecutionContext> stream) {

            Stream<DataScrapperExecutionContext> subStream = createSubStream(stream);
            for (Step child : children)
                subStream = child.build(subStream);

            return subStream.buffer().map(results -> {
                if (results.isEmpty())
                    return context(fieldName, null, null);
                List<Object> data = results.stream()
                        .map(DataScrapperExecutionContext::data)
                        .collect(Collectors.toList());
                return results.get(0).withData(data);
            });
        }

        private Stream<DataScrapperExecutionContext> createSubStream(Stream<DataScrapperExecutionContext> stream) {
            return create(subscriber ->
                    stream.observeError(Throwable.class, (o, e) -> subscriber.onError(t))
                            .observeComplete(v -> subscriber.onComplete())
                            .consume(subscriber::onNext));
        }

    }
}
