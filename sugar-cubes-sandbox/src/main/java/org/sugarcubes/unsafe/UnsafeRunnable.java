package org.sugarcubes.unsafe;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface UnsafeRunnable<T> extends UnsafeCallable<T> {

    void run() throws Throwable;

    default T returnValue() {
        return null;
    }

    @Override
    default T call() throws Throwable {
        run();
        return returnValue();
    }

}
