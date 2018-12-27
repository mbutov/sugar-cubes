package org.sugarcubes.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Three-arguments function.
 *
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface TernaryFunction<T, U, V, R> {

    R apply(T t, U u, V v);

    default <S> TernaryFunction<T, U, V, S> andThen(Function<? super R, ? extends S> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v) -> after.apply(apply(t, u, v));
    }

}
