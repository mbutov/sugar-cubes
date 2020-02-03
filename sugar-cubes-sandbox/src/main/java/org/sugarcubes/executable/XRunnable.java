package org.sugarcubes.executable;

import org.sugarcubes.rex.Rex;

/**
 * A modification of {@link Runnable} which throws exception.
 * To be used as method reference.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XRunnable<E extends Throwable> extends XExecutable<Void> {

    /**
     * Executes some code without result, or throws an exception if unable to do so.
     *
     * @throws E
     */
    void run() throws E;

    @Override
    default Void execute() {
        try {
            run();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrowAsRuntime();
        }
        return null;
    }

    /**
     * {@link Runnable} -> {@link XRunnable}
     */
    static <E extends Throwable> XRunnable<E> of(XRunnable<E> runnable) {
        return runnable;
    }

    /**
     * {@link Runnable} -> {@link XRunnable}
     */
    static <X, E extends Throwable> XRunnable<E> of(XCallable<X, E> callable) {
        return callable::call;
    }

}
