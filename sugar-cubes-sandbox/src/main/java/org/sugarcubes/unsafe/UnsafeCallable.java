package org.sugarcubes.unsafe;

import java.util.concurrent.Callable;

import org.sugarcubes.rex.Rex;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface UnsafeCallable<T> {

    T call() throws Throwable;

    default T execute() {
        try {
            return call();
        }
        catch (Throwable e) {
            throw Rex.rethrowAsRuntime(e);
        }
    }

    static <T> UnsafeCallable<T> from(Callable<T> callable) {
        return callable::call;
    }

    default Callable<T> toCallable() {
        return () -> {
            try {
                return call();
            }
            catch (Throwable e) {
                throw Rex.of(e)
                    .rethrowIfError()
                    .rethrowIf(Exception.class)
                    .rethrow(Exception::new);
            }
        };
    }

}
