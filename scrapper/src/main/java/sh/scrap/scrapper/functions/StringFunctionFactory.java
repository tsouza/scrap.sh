package sh.scrap.scrapper.functions;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.reactivestreams.Subscription;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Name("string") @Requires("to-string")
public class StringFunctionFactory implements DataScrapperFunctionFactory<Object> {
    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library,
                                       Object mainArgument, Map<String, Object> annotations) {
        return context -> subscription -> {
            Class<?> targetClass = stringUtils.contains(name) ?
                    StringUtils.class : StringEscapeUtils.class;
            try {
                Method method = findMethod(targetClass, name, mainArgument, annotations);
                if (method == null) {
                    subscription.onError(new IllegalArgumentException("Unknown function [string:" + name + "]"));
                    subscription.onComplete();
                    return;
                }
                String[] paramIndexes = discoverer.getParameterNames(method);
                subscription.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        Object data = context.data();
                        Object[] invokeArgs = new Object[method.getParameterCount()];
                        invokeArgs[0] = data;
                        if (method.getParameterCount() > 1)
                            invokeArgs[1] = mainArgument;
                        for (int idx = method.getParameterCount() > 1 ? 2 : 1; idx < paramIndexes.length; idx++)
                            invokeArgs[idx] = annotations.get(paramIndexes[idx]);

                        try {
                            try {
                                data = method.invoke(method.getDeclaringClass(), invokeArgs);
                            } catch (InvocationTargetException e) {
                                throw e.getTargetException();
                            }
                        } catch (Throwable e) {
                            subscription.onError(e);
                            subscription.onComplete();
                            return;
                        }

                        subscription.onNext(context.withData(data));
                        subscription.onComplete();
                    }

                    @Override
                    public void cancel() {
                    }
                });
            } catch (IllegalArgumentException e) {
                subscription.onError(e);
                subscription.onComplete();
            }
        };
    }

    private Method findMethod(Class<?> targetClass, String methodName,
                              Object mainArgument, Map<String, Object> annotations) {
        if (mainArgument == null)
            return MethodUtils.getMatchingAccessibleMethod(targetClass, methodName, String.class);
        else if (annotations.size() == 0)
            return MethodUtils.getMatchingAccessibleMethod(targetClass, methodName,
                    String.class, mainArgument.getClass());

        Method candidate = null;
        for (Method method : targetClass.getMethods())
            if (method.getName().equals(methodName)) {
                candidate = method;
                String[] names = discoverer.getParameterNames(method);
                if (names.length > 2) {
                    annotations.put(names[1], mainArgument);
                    List<String> paramNames = Arrays.asList(names);
                    if (paramNames.containsAll(annotations.keySet()) &&
                            annotations.keySet().contains(paramNames))
                        return candidate;
                } else
                    return candidate;
            }

        if (candidate == null)
            throw new IllegalArgumentException("Unknown function [" + methodName + "]");

        return candidate;
    }

    @Override
    public boolean isValidName(String name) {
        return stringUtils.contains(name) ||
                stringEscapeUtils.contains(name);
    }

    private static final ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
    private static final Map<String, Method> methods = new HashMap<>();
    private static final Set<String> stringUtils = new HashSet<>();
    private static final Set<String> stringEscapeUtils = new HashSet<>();


    static {
        for (Method method : StringUtils.class.getMethods()) {
            methods.put(key(method), method);
            stringUtils.add(method.getName());
        }

        for (Method method : StringEscapeUtils.class.getMethods()) {
            methods.put(key(method), method);
            stringEscapeUtils.add(method.getName());
        }
    }

    private static String key(Method method) {
        String[] names = discoverer.getParameterNames(method);
        if (names == null)
            names = new String[0];
        List<String> args = new ArrayList<>(Arrays.asList(names));
        if (args.size() >= 2)
            args.set(1, "main");
        Collections.sort(args);
        return method.getName() + ":" + args;
    }

}
