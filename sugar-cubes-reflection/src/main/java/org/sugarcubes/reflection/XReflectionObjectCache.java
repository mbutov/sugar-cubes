package org.sugarcubes.reflection;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Cache implementation with weak keys and soft values.
 *
 * @author Q-MBU
 */
public class XReflectionObjectCache<K, V> extends AbstractMap<K, V> {

    private final Map<K, Reference<V>> cache = new WeakHashMap<>();

    private V value(Reference<V> ref) {
        return ref != null ? ref.get() : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return value(cache.get(key));
    }

    @Override
    public V put(K key, V value) {
        return value(cache.put(key, new SoftReference<>(value)));
    }

    @Override
    public V remove(Object key) {
        return value(cache.remove(key));
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
