package org.sugarcubes.cache;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Maxim Butov
 */
public class SharedObjectsPool {

    static class InstanceHolder {

        static final SharedObjectsPool INSTANCE = new SharedObjectsPool();

    }

    public static <T> T global(T object) {
        return InstanceHolder.INSTANCE.pooled(object);
    }

    private final Map<Object, Object> pool = WeakKeysCaches.weakValues();

    public <T> T pooled(T object) {
        return object == null ? null : (T) pool.computeIfAbsent(object, Function.identity());
    }

}
