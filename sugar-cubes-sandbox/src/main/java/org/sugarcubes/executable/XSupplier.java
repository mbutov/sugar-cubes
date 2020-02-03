package org.sugarcubes.executable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Extended {@link Supplier}.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XSupplier<T> extends Supplier<T> {

    default Runnable asRunnable() {
        return this::get;
    }

    default Callable<T> asCallable() {
        return () -> {
            try {
                return get();
            }
            catch (XRuntimeException e) {
                try {
                    throw e.getCause();
                }
                catch (Error | Exception orig) {
                    throw orig;
                }
                catch (Throwable throwable) {
                    throw e;
                }
            }
        };
    }

}
