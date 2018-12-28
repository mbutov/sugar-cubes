package org.sugarcubes.cache;

import java.lang.ref.Reference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * Cache implementation with weak keys and reference of values.
 *
 * @author Maxim Butov
 */
public class WeakKeysReferenceCache<K, V> extends AbstractMap<K, V> {

    private final Map<K, Reference<V>> cache = new WeakHashMap<>();

    private final Function<V, Reference<V>> referenceFactory;

    public WeakKeysReferenceCache(Function<V, Reference<V>> referenceFactory) {
        this.referenceFactory = referenceFactory;
    }

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
        return valueOf(cache.put(key, referenceFactory.apply(value)));
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
