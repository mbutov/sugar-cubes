package org.sugarcubes.executable;

/**
 * A modification of {@link Runnable} which throws exception.
 *
 * To be used as method reference of void methods.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface XRunnable<E extends Throwable> extends XSupplier<Void> {

    /**
     * Executes some code without result, or throws an exception if unable to do so.
     *
     * @throws E
     */
    void run() throws E;

    @Override
    default Void get() {
        try {
            run();
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new XRuntimeException(e);
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
