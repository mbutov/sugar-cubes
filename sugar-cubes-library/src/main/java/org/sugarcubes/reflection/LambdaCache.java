package org.sugarcubes.reflection;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Maxim Butov
 */
public class LambdaCache {

    interface SerializableSupplier<T> extends Supplier<T>, Serializable {

    }

    private final Map<Supplier<?>, Object> cache = new IdentityHashMap<>();

    public <T> T computeIfAbsent(SerializableSupplier<T> supplier) {
        return (T) cache.computeIfAbsent(supplier, Supplier::get);
//        SerializedLambda meta = XReflectionUtils.execute(() ->
//            XReflectionUtils.tryToMakeAccessible(supplier.getClass().getDeclaredMethod("writeReplace"))
//                .invoke(supplier));
//        String key = meta.getImplMethodName();
//        T value = (T) cache.get(key);
//        if (value == null) {
//            value = supplier.get();
//            cache.put(key, value);
//        }
//        return value;
    }

}
