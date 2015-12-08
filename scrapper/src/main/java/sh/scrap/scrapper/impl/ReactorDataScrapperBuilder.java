package sh.scrap.scrapper.impl;

import org.reactivestreams.Publisher;
import reactor.Environment;
import reactor.rx.Stream;
import sh.scrap.scrapper.*;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

import static reactor.rx.Streams.create;

public class ReactorDataScrapperBuilder implements DataScrapperBuilder {

    static { Environment.initialize(); }

    private static final Map<String, DataScrapperFunctionFactory> factories = lookupFactories();

    protected final JSLibrary library;
    private final Map<String, List<Step>> fieldSteps = new HashMap<>();

    public ReactorDataScrapperBuilder() {
        library = new JSLibrary();
    }

    @Override
    public Field field(String fieldName) {
        List<Step> steps = new ArrayList<>();
        fieldSteps.put(fieldName, steps);
        return new FieldBuilder(fieldName, steps);
    }

    @Override
    public DataScrapperBuilder addLibrarySource(String source) throws ScriptException {
        library.engine.eval(source);
        return this;
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
                        .when(Throwable.class, subscriber::onError)
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
            factories.put(factory.getClass().getAnnotation(Name.class).value(),
                    new NullWrapper(factory));

        return factories;
    }

    private static class JSLibrary implements DataScrapperFunctionLibrary {

        final ScriptEngine engine = new ScriptEngineManager().
                getEngineByName("JavaScript");

        @Override
        public Object invoke(String functionName, Object data, Object... args) throws Exception {
            Invocable inv = (Invocable) engine;
            List<Object> newArgs = new ArrayList<>(args.length + 1);
            newArgs.add(data);
            if (args.length > 0)
                newArgs.addAll(Arrays.asList(args));
            return inv.invokeFunction(functionName, newArgs.toArray());
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
            steps.add(new FunctionStep(createFunction("to-object")));
            DataScrapperFunction function = createFunction(functionName, args);
            if (function.getClass().isAnnotationPresent(Requires.class))
                for (String requiredFunction : function.getClass().getAnnotation(Requires.class).value())
                    steps.add(new FunctionStep(createFunction(requiredFunction)));
            steps.add(new FunctionStep(function));
            return this;
        }

        @Override
        public Field forEach() {
            steps.add(new FunctionStep(createFunction("to-iterable")));
            steps.add(new FunctionStep(createFunction("for-each")));
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
                    stream.dispatchOn(Environment.get())
                            .when(Throwable.class, subscriber::onError)
                            .observeComplete(v -> subscriber.onComplete())
                            .consume(subscriber::onNext));
        }

    }
}
