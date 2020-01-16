package org.sugarcubes.builder;

import java.util.function.Supplier;

import org.sugarcubes.check.Check;

/**
 * {@link Builder}s factory.
 *
 * @author Maxim Butov
 */
public class Builders {

    /**
     * {@link Builder} for constant value.
     *
     * NOTE: If you call {@link Builder#build()} several times - the builder will apply modifications to the same object.
     *
     * @param value initial value for builder
     *
     * @return builder returning {@code value}
     */
    public static <T> Builder<T> of(T value) {
        Check.arg().notNull(value, Check.format("value must not be null"));
        return () -> value;
    }

    /**
     * Wraps {@link Supplier} with {@link Builder}.
     *
     * @param supplier supplier
     *
     * @return builder returning {@code supplier.get()}
     */
    public static <T> Builder<T> of(Supplier<T> supplier) {
        return supplier::get;
    }

}
