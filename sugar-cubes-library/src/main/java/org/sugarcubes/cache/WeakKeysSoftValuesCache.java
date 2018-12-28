package org.sugarcubes.cache;

import java.lang.ref.SoftReference;

/**
 * Cache implementation with weak keys and soft values.
 *
 * @author Maxim Butov
 */
public class WeakKeysSoftValuesCache<K, V> extends WeakKeysReferencedValuesCache<K, V> {

    public WeakKeysSoftValuesCache() {
        super(SoftReference::new);
    }

}
