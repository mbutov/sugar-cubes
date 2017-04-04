package org.sugarcubes.concurrent;

/**
 * A modification of {@link Runnable} which throws exception.
 * To be used as method reference.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XRunnable<E extends Exception> {

    /**
     * Executes some code without result, or throws an exception if unable to do so.
     *
     * @throws E
     */
    void run() throws E;

    /**
     * {@link Runnable} -> {@link XRunnable}
     */
    static XRunnable<RuntimeException> of(Runnable runnable) {
        return runnable::run;
    }

}
