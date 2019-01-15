package org.sugarcubes.builder;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * {@link Builder}-related static helpers.
 *
 * @author Maxim Butov
 */
public class Builders {

    /**
     * {@link Builder} for constant value.
     *
     * @param value value
     * @return builder
     */
    public static <T> Builder<T> of(T value) {
        Objects.requireNonNull(value, "value must not be null");
        return () -> value;
    }

    /**
     * Wraps {@link Supplier} with {@link Builder}.
     *
     * @param supplier supplier
     * @return builder
     */
    public static <T> Builder<T> of(Supplier<T> supplier) {
        return supplier::get;
    }

}
