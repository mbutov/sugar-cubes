package org.sugarcubes.executable;

import java.util.concurrent.Callable;

import org.sugarcubes.rex.Rex;

/**
 * The variant of {@link Callable} which throws specific exception instead of {@link Exception}.
 * To be used as method reference.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XCallable<T, E extends Throwable> extends XExecutable<T> {

    @Override
    default T execute() {
        try {
            return call();
        }
        catch (Throwable e) {
            throw Rex.rethrowAsRuntime(e);
        }
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     *
     * @throws E if unable to compute a result
     */
    T call() throws E;

    @Override
    default Callable<T> asCallable() {
        return () -> {
            try {
                return call();
            }
            catch (Error | Exception e) {
                throw e;
            }
            catch (Throwable e) {
                throw Rex.rethrowAsRuntime(e);
            }
        };
    }

    /**
     * {@link XCallable} of method reference
     */
    static <T, E extends Throwable> XCallable<T, E> of(XCallable<T, E> callable) {
        return callable;
    }

    /**
     * {@link XRunnable} -> {@link XCallable}
     */
    static <T, E extends Throwable> XCallable<T, E> of(XRunnable<E> runnable) {
        return of(runnable, null);
    }

    /**
     * {@link XRunnable} -> {@link XCallable}
     */
    static <T, E extends Throwable> XCallable<T, E> of(XRunnable<E> runnable, T result) {
        return () -> {
            runnable.run();
            return result;
        };
    }

}
