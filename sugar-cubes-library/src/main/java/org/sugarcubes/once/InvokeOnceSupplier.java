package org.sugarcubes.once;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.sugarcubes.check.Checks;

/**
 * Хелпер для вызова функции, предотвращающий повторное выполнение кода в случае одновременного вызова из нескольких потоков.
 *
 * @author Maxim Butov
 */
public class InvokeOnceSupplier<T> implements Supplier<T> {

    private final Callable<T> supplier;
    private volatile InvocationResult<T> result;

    public InvokeOnceSupplier(Callable<T> supplier) {
        Checks.arg().notNull(supplier, "Supplier must not be null.");
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return result == null ? supply() : result.getValue();
    }

    private synchronized T supply() {
        if (result == null) {
            result = InvocationResult.execute(supplier);
        }
        return result.getValue();
    }

}
