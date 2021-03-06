/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Thiago Souza <thiago@scrap.sh>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package sh.scrap.scrapper.core;

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
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static reactor.rx.Streams.create;

public class ReactorDataScrapperBuilder implements DataScrapperBuilder, Serializable {

    static { Environment.initializeIfEmpty(); }

    private static final Map<String, DataScrapperFunctionFactory> factories = lookupFactories();

    protected transient JSLibrary library;
    private final Map<String, Deque<Step>> fieldSteps = new HashMap<>();
    private final Map<String, FieldType> fieldTypes = new HashMap<>();
    private final Set<String> arrayTypes = new HashSet<>();
    private final Set<String> librarySource = new HashSet<>();

    public ReactorDataScrapperBuilder() {}

    @Override
    public Field field(String fieldName) {
        Deque<Step> steps = fieldSteps.containsKey(fieldName) ?
                fieldSteps.get(fieldName) : new LinkedList<>();
        fieldSteps.put(fieldName, steps);
        return new FieldBuilder(fieldName, steps);
    }

    @Override
    public DataScrapperBuilder addLibrarySource(String source) throws ScriptException {
        //library.engine.eval(source);
        librarySource.add(source);
        return this;
    }

    @Override
    public DataScrapper build() {

        for (Deque<Step> steps : fieldSteps.values())
            steps.getLast().onBuild(steps);

        return (metadata, data) -> create(build(metadata, data))
            .buffer(fieldSteps.size())
            .map(results -> {
                Map<String, Object> output = new HashMap<>();
                results.forEach(context -> output.put(context.fieldName(), context.data()));
                return output;
            })
        .next();
    }

    private Publisher<DataScrapperExecutionContext> build(Map<String, Object> metadata, Object data) {
        return subscriber -> fieldSteps.entrySet().stream()
                .forEach(entry -> build(entry.getValue(), context(entry.getKey(), metadata, data))
                        .when(Throwable.class, subscriber::onError)
                        .consume(subscriber::onNext));
    }

    private Stream<DataScrapperExecutionContext> build(Deque<Step> steps, DataScrapperExecutionContext context) {
        Stream<DataScrapperExecutionContext> stream = create(subscriber -> {
            subscriber.onNext(context);
            subscriber.onComplete();
        });
        for (Step step : steps)
            stream = step.build(stream);
        return stream;
    }

    public static Map<String, DataScrapperFunctionFactory> lookupFactories() {

        ServiceLoader<DataScrapperFunctionFactory> loader = ServiceLoader.load(DataScrapperFunctionFactory.class);

        Map<String, DataScrapperFunctionFactory> factories = new HashMap<>();

        for (DataScrapperFunctionFactory factory : loader)
            factories.put(factory.getClass().getAnnotation(Name.class).value(),
                    new NullWrapper(factory));

        return factories;
    }

    public boolean isValidFunctionName(String text) {
        String[] p = text.split("\\.");
        boolean exists = factories.containsKey(p[0]);
        if (p.length == 1 || !exists)
            return exists;

        return factories.get(p[0]).isValidName(p[1]);
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

    private static class NullWrapper implements DataScrapperFunctionFactory<Object> {

        private final DataScrapperFunctionFactory wrapped;

        private NullWrapper(DataScrapperFunctionFactory wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean isValidName(String name) {
            return wrapped.isValidName(name);
        }

        @Override @SuppressWarnings("unchecked")
        public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                           Object mainArgument, Map<String, Object> annotations) {
            DataScrapperFunction function = wrapped.create(name, library, mainArgument, annotations);
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
            public void objectProcessed(Object object) {
            }

            @Override
            public DataScrapperExecutionContext withData(Object data) {
                return context(fieldName, metadata, data);
            }

        };
    }

    private DataScrapperFunction createFunction(String name) {
        return createFunction(name, null, Collections.emptyMap());
    }

    @SuppressWarnings("unchecked")
    private DataScrapperFunction createFunction(String name, Object mainArgument, Map<String, Object> annotations) {
        return new DataScrapperFunctionProxy(name, mainArgument, annotations);
    }

    private DataScrapperFunctionFactory getFactory(String name) {
        return ((NullWrapper) factories.get(name.split(NAMESPACE_SEPARATOR)[0])).wrapped;
    }

    protected JSLibrary getLibrary() {
        try {
            if (library == null) {
                library = new JSLibrary();
                for (String source : librarySource)
                    library.engine.eval(source);
            }
            return library;
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    class FieldBuilder implements Field, Serializable {

        private final String fieldName;
        private final Deque<Step> steps;

        FieldBuilder(String fieldName, Deque<Step> steps) {
            this.fieldName = fieldName;
            this.steps = steps;
            if (!fieldTypes.containsKey(fieldName))
                castTo(FieldType.STRING);
        }

        @Override
        public Field castTo(FieldType type) {
            fieldTypes.put(fieldName, type);
            return this;
        }

        @Override
        public Field asArray() {
            arrayTypes.add(fieldName);
            return this;
        }

        @Override
        public Field map(String functionName) {
            return map(functionName, null);
        }

        @Override
        public Field map(String functionName, Object mainArgument) {
            return map(functionName, mainArgument, Collections.emptyMap());
        }

        @Override
        public Field map(String functionName, Object mainArgument, Map<String, Object> annotations) {
            annotations = annotations == null ?
                    Collections.emptyMap() : annotations;

            steps.addLast(new FunctionStep(fieldName, createFunction("to-object")));
            DataScrapperFunction function = createFunction(functionName, mainArgument, annotations);
            DataScrapperFunctionFactory factory = getFactory(functionName);
            if (factory.getClass().isAnnotationPresent(Requires.class))
                for (String requiredFunction : factory.getClass().getAnnotation(Requires.class).value())
                    map(requiredFunction);
            steps.addLast(new FunctionStep(fieldName, function));
            return this;
        }

        @Override
        public Field forEach() {
            steps.addLast(new FunctionStep(fieldName, createFunction("to-iterable")));
            steps.addLast(new FunctionStep(fieldName, createFunction("for-each")));
            CompositeStep step = new CompositeStep(fieldName);
            steps.addLast(step);
            return new FieldBuilder(fieldName, step.children);
        }

        @Override
        public DataScrapper build() {
            return ReactorDataScrapperBuilder.this.build();
        }

    }

    interface Step extends Serializable {
        Stream<DataScrapperExecutionContext> build(Stream<DataScrapperExecutionContext> stream);
        void onBuild(Deque<Step> steps);
    }

    class FunctionStep implements Step {

        final String fieldName;
        final DataScrapperFunction function;

        FunctionStep(String fieldName, DataScrapperFunction function) {
            this.fieldName = fieldName;
            this.function = function;
        }

        @Override
        public Stream<DataScrapperExecutionContext> build(Stream<DataScrapperExecutionContext> stream) {
            return stream.map(function::process).merge();
        }

        @Override
        public void onBuild(Deque<Step> steps) {
            boolean isArray = arrayTypes.contains(fieldName);
            steps.addLast(new FunctionStep(fieldName,
                    createFunction(isArray ? "for-each" : "to-object")));
            steps.addLast(new FunctionStep(fieldName, createFunction("to-" + typeCast())));
        }

        private String typeCast() {
            return fieldTypes.get(fieldName).name().toLowerCase();
        }

    }

    class CompositeStep implements Step {

        final String fieldName;
        final Deque<Step> children = new LinkedList<>();

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

        @Override
        public void onBuild(Deque<Step> steps) {
            if (!children.isEmpty())
                children.getLast().onBuild(children);
        }

        private Stream<DataScrapperExecutionContext> createSubStream(Stream<DataScrapperExecutionContext> stream) {
            return create(subscriber ->
                    stream.dispatchOn(Environment.get())
                            .when(Throwable.class, subscriber::onError)
                            .observeComplete(v -> subscriber.onComplete())
                            .consume(subscriber::onNext));
        }

    }

    public class DataScrapperFunctionProxy implements DataScrapperFunction, Serializable {

        private final String functionName;
        private final Object mainArgument;
        private final Map<String, Object> annotations;

        public DataScrapperFunctionProxy(String functionName, Object mainArgument, Map<String, Object> annotations) {
            this.functionName = functionName;
            this.mainArgument = mainArgument;
            this.annotations = annotations;
        }

        @Override @SuppressWarnings("unchecked")
        public Publisher<DataScrapperExecutionContext> process(DataScrapperExecutionContext context) {
            String[] n = functionName.split(NAMESPACE_SEPARATOR);
            if (n.length == 1)
                return factories.get(n[0])
                        .create(n[0], getLibrary(), mainArgument, annotations)
                        .process(context);
            return factories.get(n[0])
                    .create(n[1], getLibrary(), mainArgument, annotations)
                    .process(context);
        }
    }

    private static final String NAMESPACE_SEPARATOR = "\\:";

}
