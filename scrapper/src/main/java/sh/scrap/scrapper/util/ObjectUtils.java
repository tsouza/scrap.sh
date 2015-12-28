package sh.scrap.scrapper.util;

import sh.scrap.scrapper.functions.ToStringFunctionFactory;

import java.util.Map;

public final class ObjectUtils {

    @SuppressWarnings("unchecked")
    public static int sizeOf(Object data) {
        if (data == null)
            return 0;

        if (data instanceof String)
            return ((String) data).getBytes().length;

        if (data instanceof Object[]) {
            int result = 0;
            Object[] ar = (Object[]) data;
            for (Object o : ar)
                result += sizeOf(o);
            return result;
        }

        if (data instanceof Iterable) {
            int result = 0;
            Iterable<Object> it = (Iterable<Object>) data;
            for (Object o : it)
                result += sizeOf(o);
            return result;
        }

        if (data instanceof Map) {
            int result = 0;
            Map<Object, Object> m = (Map<Object, Object>) data;
            for (Map.Entry<Object, Object> e : m.entrySet()) {
                result += sizeOf(e.getKey());
                result += sizeOf(e.getValue());
            }
            return result;
        }

        try {
            return sizeOf(ToStringFunctionFactory.toString(data));
        } catch (Exception e) {
            return 0;
        }
    }

    private ObjectUtils() {}
}
