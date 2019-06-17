package org.sugarcubes.builder.collection;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import org.sugarcubes.builder.MutableBuilder;

/**
 * Map builders.
 *
 * @author Maxim Butov
 */
public class MapBuilder<K, V, M extends Map<K, V>> extends MutableBuilder<M, MapBuilder<K, V, M>> {

    private boolean errorOnDuplicateKeys = false;

    public MapBuilder(Supplier<M> supplier) {
        super(supplier);
    }

    public MapBuilder<K, V, M> errorOnDuplicateKeys() {
        errorOnDuplicateKeys = true;
        return self();
    }

    public <D extends Map<K, V>> MapBuilder<K, V, D> cast() {
        return (MapBuilder) this;
    }

    public MapBuilder<K, V, M> put(K key, V value) {
        return apply(map -> {
            V previous = map.put(key, value);
            if (errorOnDuplicateKeys && previous != null) {
                throw new IllegalArgumentException("Duplicate values for key " + key);
            }
        });
    }

    public MapBuilder<K, V, M> putAll(Map<K, V> m) {
        m.forEach(this::put);
        return self();
    }

    /// STATIC STUFF

    public static <K, V, M extends Map<K, V>> MapBuilder<K, V, M> map(Supplier<M> supplier) {
        return new MapBuilder<>(supplier);
    }

    public static <K, V> MapBuilder<K, V, Map<K, V>> hashMap() {
        return map(HashMap::new);
    }

    public static <K, V> MapBuilder<K, V, Map<K, V>> linkedHashMap() {
        return map(LinkedHashMap::new);
    }

    public static <K, V> MapBuilder<K, V, NavigableMap<K, V>> treeMap() {
        return map(TreeMap::new);
    }

    public static <K, V> MapBuilder<K, V, Map<K, V>> identityHashMap() {
        return map(IdentityHashMap::new);
    }

    public static <K, V> MapBuilder<K, V, Map<K, V>> weakHashMap() {
        return map(WeakHashMap::new);
    }

}
