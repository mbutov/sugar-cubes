package org.sugarcubes.reflection;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * Factory for xreflection objects.
 *
 * @author Maxim Butov
 */
public class XReflection {

    /**
     * Cache for xreflection objects.
     */
    private static final Map<Object, WeakReference> CACHE = new WeakHashMap<>();

    private static <K, V> V getFromCache(K key, Function<K, V> mappingFunction) {
        Map<K, WeakReference<V>> cache = (Map) CACHE;
        WeakReference<V> reference = cache.get(key);
        V value = reference != null ? reference.get() : null;
        if (value == null) {
            value = mappingFunction.apply(key);
            CACHE.put(key, new WeakReference<>(value));
        }
        return value;
    }

    public static <T> XClass<T> of(Class<T> clazz) {
        return getFromCache(clazz, XClass::new);
    }

    public static <X> XConstructor<X> of(Constructor<X> constructor) {
        return getFromCache(constructor, XConstructor::new);
    }

    public static <X> XMethod<X> of(Method method) {
        return getFromCache(method, XMethod::new);
    }

    public static <X> XField<X> of(Field field) {
        // XField can modify state of field, so don't cache them
        return new XField<>(field);
    }

}
