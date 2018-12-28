package org.sugarcubes.cache;

import java.lang.ref.Reference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * <p>Cache implementation based on {@link WeakHashMap} with weak keys and referenced ({@link Reference}) values.</p>
 *
 * <p>This class does not implement:
 * <il>
 *     <li>{@link java.util.Map#entrySet()}</li>
 *     <li>{@link java.util.Map#keySet()}</li>
 *     <li>{@link java.util.Map#values()}</li>
 *     <li>{@link java.util.Map#size()}</li>
 *     <li>{@link java.util.Map#isEmpty()}</li>
 *     <li>{@link java.util.Map#containsValue(Object)}</li>
 * </il>
 * Calling these methods will cause {@link UnsupportedOperationException}.</p>
 *
 * @see WeakHashMap
 * @see Reference
 *
 * @author Maxim Butov
 */
public class WeakKeysReferencedValuesCache<K, V> extends AbstractMap<K, V> {

    private final Map<K, Reference<V>> cache = new WeakHashMap<>();
    private final Function<V, ? extends Reference<V>> referenceFactory;

    public WeakKeysReferencedValuesCache(Function<V, ? extends Reference<V>> referenceFactory) {
        this.referenceFactory = referenceFactory;
    }

    private Reference<V> wrap(V value) {
        return referenceFactory.apply(value);
    }

    private V unwrap(Reference<V> reference) {
        return reference != null ? reference.get() : null;
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return unwrap(cache.get(key));
    }

    @Override
    public V put(K key, V value) {
        return unwrap(cache.put(key, wrap(value)));
    }

    @Override
    public V remove(Object key) {
        return unwrap(cache.remove(key));
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
