package org.sugarcubes.builder;

import java.util.function.Supplier;

/**
 * Same as {@link MutableBuilder} with just one type parameter.
 *
 * <pre>
 *      new GenericBuilder<>()
 * </pre>
 *
 * @author Maxim Butov
 */
public class GenericBuilder<T> extends MutableBuilder<T, GenericBuilder<T>> {

    public GenericBuilder(T value) {
        super(value);
    }

    public GenericBuilder(Supplier<T> supplier) {
        super(supplier);
    }

}
