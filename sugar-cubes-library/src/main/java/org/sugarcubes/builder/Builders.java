package org.sugarcubes.builder;

import java.util.function.Supplier;

import org.sugarcubes.arg.Arg;

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
        Arg.notNull(value, "value must not be null");
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
