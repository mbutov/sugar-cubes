package org.sugarcubes.builder;

import java.util.function.Consumer;

/**
 * Three-arguments {@link Consumer}.
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ThreeConsumer<T, U, V> {

    void accept(T t, U u, V v);

}
