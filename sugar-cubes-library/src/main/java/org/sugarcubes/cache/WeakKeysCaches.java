package org.sugarcubes.cache;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.function.Function;

/**
 * Caches based on {@link java.util.WeakHashMap}.
 *
 * These caches can be used without any cleaning up. GC takes care of them.
 *
 * These caches also do not guarantee the presence of previously cached entries.
 *
 * @author Maxim Butov
 */
public class WeakKeysCaches {

    /**
     * Cache implementation with weak keys and custom referenced values.
     *
     * @param referenceFactory factory for reference creation
     *
     * @return cache implementation {@link WeakKeysReferencedValuesCache}
     */
    private static <K, V> Map<K, V> referenceValues(Function<V, ? extends Reference<V>> referenceFactory) {
        return new WeakKeysReferencedValuesCache<>(referenceFactory);
    }

    /**
     * Cache implementation with weak keys and soft values.
     *
     * @return cache implementation {@link WeakKeysReferencedValuesCache}
     *
     * @see WeakKeysReferencedValuesCache
     * @see SoftReference
     */
    public static <K, V> Map<K, V> softValues() {
        return referenceValues(SoftReference::new);
    }

    /**
     * Cache implementation with weak keys and weak values.
     *
     * @return cache implementation {@link WeakKeysReferencedValuesCache}
     *
     * @see WeakKeysReferencedValuesCache
     * @see WeakReference
     */
    public static <K, V> Map<K, V> weakValues() {
        return referenceValues(WeakReference::new);
    }

}
