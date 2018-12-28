package org.sugarcubes.cache;

import java.lang.ref.WeakReference;

/**
 * Cache implementation with weak keys and weak values.
 *
 * @author Maxim Butov
 */
public class WeakKeysWeakValuesCache<K, V> extends WeakKeysReferencedValuesCache<K, V> {

    public WeakKeysWeakValuesCache() {
        super(WeakReference::new);
    }

}
