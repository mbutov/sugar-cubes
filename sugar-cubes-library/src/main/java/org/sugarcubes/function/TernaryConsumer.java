package org.sugarcubes.function;

import java.util.Objects;

/**
 * Three-arguments consumer.
 *
 * @see java.util.function.Consumer
 * @see java.util.function.BiConsumer
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface TernaryConsumer<T, U, V> {

    void accept(T t, U u, V v);

    default TernaryConsumer<T, U, V> andThen(TernaryConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> {
            accept(t, u, v);
            after.accept(t, u, v);
        };
    }

}
