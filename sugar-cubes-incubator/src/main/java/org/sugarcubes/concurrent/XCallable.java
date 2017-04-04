package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;

/**
 * The variant of {@link Callable} which throws specific exception instead of {@link Exception}.
 * To be used as method reference.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XCallable<T, E extends Exception> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws E if unable to compute a result
     */
    T call() throws E;

    /**
     * {@link Callable} -> {@link XCallable}
     */
    static <T> XCallable<T, Exception> of(Callable<T> callable) {
        return callable::call;
    }

}
