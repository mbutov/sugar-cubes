package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import org.sugarcubes.rex.Rex;

/**
 * Extension of {@link Callable}, which has a save version of {@link #call()} - {@link #execute()},
 * and may be converted to {@link Runnable}.
 *
 * This class is also parametrized with exception.
 *
 * @author Maxim Butov
 */
public interface Executable<T, E extends Exception> extends Callable<T>, XCallable<T, E> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws E if unable to compute a result
     */
    @Override
    T call() throws E;

    /**
     * Invokes {@link #call()}, returns a result or throws {@link RuntimeException}.
     *
     * @return result of {@link #call()} invocation
     */
    default T execute() {
        try {
            return call();
        }
        catch (Exception e) {
            throw Rex.throwUnchecked(e);
        }
    }

    /**
     * Creates {@link Runnable}, which calls {@link #execute()}, result is ignored.
     *
     * @return {@link Runnable}
     */
    default Runnable toRunnable() {
        return this::execute;
    }

    /**
     * Creates {@link Supplier}, which calls {@link #execute()} and returns result.
     *
     * @return {@link Supplier}
     */
    default Supplier<T> toSupplier() {
        return this::execute;
    }

    /**
     * Creates another {@link Executable} which calls this one and then transforms the result.
     * @param transformer result transformer
     * @return new {@link Executable}
     */
    default <V> Executable<V, E> transform(Function<T, V> transformer) {
        return () -> transformer.apply(call());
    }

    /**
     * Replaces the result of the original {@link Executable} with the result of {@code supplier}.
     *
     * @param supplier suppler for the result
     * @return new {@link Executable}
     */
    default <V> Executable<V, E> replace(Supplier<V> supplier) {
        return transform(r -> supplier.get());
    }

    /**
     * Replaces the result of the original {@link Executable} with the {@code result}.
     *
     * @param result result
     * @return new {@link Executable}
     */
    default <V> Executable<V, E> resulting(V result) {
        return replace(() -> result);
    }

    /**
     * {@link Callable} -> {@link Executable}
     */
    static <T> Executable<T, Exception> of(Callable<T> callable) {
        return callable::call;
    }

    /**
     * {@link Runnable} -> {@link Executable}
     */
    static <T> Executable<T, RuntimeException> of(Runnable runnable) {
        return ofx(XRunnable.of(runnable));
    }

    /**
     * {@link Supplier} -> {@link Executable}
     */
    static <T> Executable<T, RuntimeException> of(Supplier<T> supplier) {
        return new Executable<T, RuntimeException>() {
            @Override
            public T call() {
                return supplier.get();
            }
        };
    }

    /**
     * {@link XRunnable} -> {@link Executable}
     */
    static <T, E extends Exception> Executable<T, E> ofx(XRunnable<E> xRunnable) {
        return () -> {
            xRunnable.run();
            return null;
        };
    }

    /**
     * {@link XCallable} -> {@link Executable}
     */
    static <T, E extends Exception> Executable<T, E> ofx(XCallable<T, E> callable) {
        return callable::call;
    }

}
