package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Утилиты для копирования thread local значений в другой поток.
 *
 * @author Maxim Butov
 */
public class ConcurrentUtils {

    /**
     * Копирует (в общем виде) значения к.-л. thread locals из текущего потока в поток, в котором будет вызываться
     * {@code task}, после вызова восстанавливает в потоке предыдущие значения.
     *
     * @param parentSupplier {@link Supplier} значения для вызова в исходном потоке, возвращает значение, которое нужно передать
     * в дочерний поток
     * @param childSupplier {@link Supplier} значения для вызова в дочернем потоке, возвращает значение, которое нужно сохранить
     * для последующего восстановления
     * @param task код для выполнения в дочернем потоке
     * @param childConsumer {@link Consumer}, принимающий значение, сохранённое в дочернем потоке, для восстановления
     *
     * @return {@link Callable} с дополнительной логикой
     */
    public static <V, X> Executable<V, Exception> copyThreadLocal(Supplier<X> parentSupplier, Supplier<X> childSupplier, Callable<V> task,
        Consumer<X> childConsumer) {
        final X parentValue = parentSupplier.get();
        return () -> {
            X childValue = childSupplier.get();
            childConsumer.accept(parentValue);
            try {
                return task.call();
            }
            finally {
                childConsumer.accept(childValue);
            }
        };
    }

    /**
     * Аналог {@link #copyThreadLocal(Supplier, Supplier, Callable, Consumer)}, но с константным значением для
     * {@code parentSupplier}.
     */
    public static <V, X> Executable<V, Exception> copyThreadLocalValue(X parentValue, Supplier<X> childSupplier, Callable<V> task,
        Consumer<X> childConsumer) {
        return copyThreadLocal(() -> parentValue, childSupplier, task, childConsumer);
    }

    /**
     * Аналог {@link #copyThreadLocal(Supplier, Supplier, Callable, Consumer)} для случая {@code parentSupplier == childSupplier}.
     */
    public static <V, X> Executable<V, Exception> copyThreadLocal(Supplier<X> supplier, Callable<V> task, Consumer<X> childConsumer) {
        return copyThreadLocal(supplier, supplier, task, childConsumer);
    }

    /**
     * Копирует (в общем виде) значения к.-л. thread locals из текущего потока в поток, в котором будет вызываться
     * {@code task}, после вызова восстанавливает в потоке предыдущие значения.
     *
     * @param parentSupplier {@link Supplier} значения для вызова в исходном потоке, возвращает значение, которое нужно передать
     * в дочерний поток
     * @param childSupplier {@link Supplier} значения для вызова в дочернем потоке, возвращает значение, которое нужно сохранить
     * для последующего восстановления
     * @param task код для выполнения в дочернем потоке
     * @param childConsumer {@link Consumer}, принимающий значение, сохранённое в дочернем потоке, для восстановления
     *
     * @return {@link Runnable} с дополнительной логикой
     */
    public static <X> Runnable copyThreadLocal(Supplier<X> parentSupplier, Supplier<X> childSupplier, Runnable task,
        Consumer<X> childConsumer) {
        return copyThreadLocal(parentSupplier, childSupplier, Executable.of(task), childConsumer).toRunnable();
    }

    /**
     * Аналог {@link #copyThreadLocal(Supplier, Supplier, Runnable, Consumer)}, но с константным значением для
     * {@code parentSupplier}.
     */
    public static <X> Runnable copyThreadLocalValue(X parentValue, Supplier<X> childSupplier, Runnable task,
        Consumer<X> childConsumer) {
        return copyThreadLocal(() -> parentValue, childSupplier, task, childConsumer);
    }

    /**
     * Аналог {@link #copyThreadLocal(Supplier, Supplier, Runnable, Consumer)} для случая {@code parentSupplier == childSupplier}.
     */
    public static <X> Runnable copyThreadLocal(Supplier<X> supplier, Runnable task, Consumer<X> childConsumer) {
        return copyThreadLocal(supplier, supplier, task, childConsumer);
    }

}
