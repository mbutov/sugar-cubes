package org.sugarcubes.cache;

import java.util.Map;
import java.util.function.Function;

/**
 * A pool for objects of any type. Can be used to reduce memory usage when working with large amount repeatable objects,
 * e.g. words in the long text.
 *
 * @author Maxim Butov
 */
public class SharedObjectsPool {

    static class InstanceHolder {

        static final SharedObjectsPool INSTANCE = new SharedObjectsPool();

    }

    /**
     * Calls {@link #pooled(Object)} of global pool.
     * @see #pooled(Object)
     */
    public static <T> T global(T object) {
        return InstanceHolder.INSTANCE.pooled(object);
    }

    private final Map<Object, Object> pool = WeakKeysCaches.weakValues();

    /**
     * Returns same object from pool if such exists. If not, puts the object into pool and returns it.
     *
     * @param object reusable object
     *
     * @return the same object from pool (if found) or the same object
     */
    public <T> T pooled(T object) {
        return object == null ? null : (T) pool.computeIfAbsent(object, Function.identity());
    }

}
