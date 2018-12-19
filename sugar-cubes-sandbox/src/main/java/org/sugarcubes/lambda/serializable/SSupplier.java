package org.sugarcubes.lambda.serializable;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * {@link Supplier} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SSupplier<T> extends Supplier<T>, Serializable {

    static <X> SSupplier<X> c(X value) {
        return () -> value;
    }

}
