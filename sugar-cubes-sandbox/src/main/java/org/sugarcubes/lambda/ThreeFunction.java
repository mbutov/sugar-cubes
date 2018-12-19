package org.sugarcubes.lambda;

import java.util.function.Function;

/**
 * Three-arguments {@link Function}.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ThreeFunction<T, U, V, R> {

    R apply(T t, U u, V v);

}
