package org.sugarcubes.builder;

import java.util.function.Supplier;

/**
 * Same as {@link MutableBuilder} with just one type parameter.
 *
 * {@code new GenericBuilder<>()}
 *
 * @author Maxim Butov
 */
public class GenericBuilder<T> extends MutableBuilder<T, GenericBuilder<T>> {

    public static <T> GenericBuilder<T> of(Supplier<T> supplier) {
        return new GenericBuilder<>(supplier);
    }

    public GenericBuilder(Supplier<T> supplier) {
        super(supplier);
    }

}
