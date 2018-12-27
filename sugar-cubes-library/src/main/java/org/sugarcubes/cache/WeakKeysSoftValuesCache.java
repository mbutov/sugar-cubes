package org.sugarcubes.cache;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Cache implementation with weak keys and soft values.
 *
 * @author Maxim Butov
 */
public class WeakKeysSoftValuesCache<K, V> extends AbstractMap<K, V> {

    private final Map<K, Reference<V>> cache = new WeakHashMap<>();

    private V valueOf(Reference<V> ref) {
        return ref != null ? ref.get() : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return valueOf(cache.get(key));
    }

    @Override
    public V put(K key, V value) {
        return valueOf(cache.put(key, new SoftReference<>(value)));
    }

    @Override
    public V remove(Object key) {
        return valueOf(cache.remove(key));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

}
