package sh.scrap.scrapper.functions;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Subscription;
import sh.scrap.scrapper.DataScrapperFunction;
import sh.scrap.scrapper.DataScrapperFunctionFactory;
import sh.scrap.scrapper.DataScrapperFunctionLibrary;
import sh.scrap.scrapper.annotation.Name;
import sh.scrap.scrapper.annotation.Requires;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Name("string") @Requires("to-string")
public class StringFunctionFactory implements DataScrapperFunctionFactory {
    @Override
    public DataScrapperFunction create(String name, DataScrapperFunctionLibrary library, Object... args) {
        return context -> subscription -> {
            Class<?> targetClass = stringUtils.contains(name) ?
                    StringUtils.class : StringEscapeUtils.class;
            try {
                Method method = findMethod(targetClass, name, args);
                subscription.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        Object data = context.data();
                        Object[] invokeArgs = new Object[method.getParameterCount()];
                        System.arraycopy(args, 0, invokeArgs, 1, args.length);
                        invokeArgs[0] = data;
                        try {
                            try {
                                data = method.invoke(targetClass, invokeArgs);
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
                    @Override public void cancel() {}
                });

            } catch (NoSuchMethodException e) {
                subscription.onError(e);
                subscription.onComplete();
            }
        };
    }

    @Override
    public boolean isValidName(String name) {
        return stringUtils.contains(name) ||
                stringEscapeUtils.contains(name);
    }

    private Method findMethod(Class<?> targetClass, String methodName, Object[] args) throws NoSuchMethodException {
        Class<?>[] argsType = new Class<?>[args.length + 1];
        argsType[0] = String.class;
        for (int i = 0; i < args.length; i++)
            argsType[i + 1] = args[i].getClass();
        return targetClass.getMethod(methodName, argsType);
    }

    private static final Set<String> stringUtils = new HashSet<>();
    private static final Set<String> stringEscapeUtils = new HashSet<>();

    static {
        for (Method method : StringUtils.class.getMethods())
            stringUtils.add(method.getName());

        for (Method method : StringEscapeUtils.class.getMethods())
            stringEscapeUtils.add(method.getName());
    }
}
