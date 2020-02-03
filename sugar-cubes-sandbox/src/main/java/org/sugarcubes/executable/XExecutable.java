package org.sugarcubes.executable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XExecutable<T> {

    T execute();

    default Runnable asRunnable() {
        return this::execute;
    }

    default Callable<T> asCallable() {
        return this::execute;
    }

    default Supplier<T> asSupplier() {
        return this::execute;
    }

    default <E extends Throwable> XCallable<T, E> asXCallable() {
        return this::execute;
    }

}
