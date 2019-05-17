package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

import org.sugarcubes.cache.WeakKeyCaches;

/**
 * Factory for xreflection objects.
 *
 * @author Maxim Butov
 */
public class XReflection {

    public static <T> XClass<T> of(Class<T> clazz) {
        return computeIfAbsent(clazz, XClass::new);
    }

    public static <X> XConstructor<X> of(Constructor<X> constructor) {
        return computeIfAbsent(constructor, XConstructor::new);
    }

    public static <X> XMethod<X> of(Method method) {
        return computeIfAbsent(method, XMethod::new);
    }

    public static <X> XField<X> of(Field field) {
        return computeIfAbsent(field, XField::new);
    }

    /**
     * Cache for xreflection objects.
     */
    private static final Map<Object, XReflectionObject> CACHE = WeakKeyCaches.softValues();

    public static <K, V> V computeIfAbsent(K key, Function<K, V> mappingFunction) {
        return ((Map<K, V>) CACHE).computeIfAbsent(key, mappingFunction);
    }

}
