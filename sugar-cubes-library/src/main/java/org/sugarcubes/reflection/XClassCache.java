package org.sugarcubes.reflection;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * Cache for {@link XClass} calculated values.
 *
 * @author Maxim Butov
 */
public class XClassCache {

    private static final Map<XClass<?>, Map> CACHE = new WeakHashMap<>();

    public static <K, V> V get(XClass<K> xClass, Function<XClass<K>, V> loader) {
        Map<Function<XClass<K>, V>, V> xClassCache = CACHE.computeIfAbsent(xClass, key -> new HashMap<>());
        return xClassCache.computeIfAbsent(loader, f -> f.apply(xClass));
    }

}
