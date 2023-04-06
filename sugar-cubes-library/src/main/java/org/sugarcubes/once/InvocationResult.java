package org.sugarcubes.once;

import java.util.concurrent.Callable;

import org.sugarcubes.rex.Rex;

/**
 * Результат выполнения функции либо исключение.
 *
 * @author Q-MBU
 */
public class InvocationResult<T> {

    public static <T> InvocationResult<T> execute(Callable<T> callable) {
        try {
            return new InvocationResult<>(callable.call(), null);
        }
        catch (Exception e) {
            return new InvocationResult<>(null, e);
        }
    }

    private final T value;
    private final Throwable exception;

    private InvocationResult(T value, Throwable exception) {
        this.value = value;
        this.exception = exception;
    }

    public T getValue() {
        if (exception != null) {
            throw Rex.rethrowAsRuntime(exception);
        }
        else {
            return value;
        }
    }

}
