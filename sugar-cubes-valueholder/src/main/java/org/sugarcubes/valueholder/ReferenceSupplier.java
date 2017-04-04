package org.sugarcubes.valueholder;

import java.lang.ref.Reference;
import java.util.function.Supplier;

/**
 * {@link Supplier} для ссылки.
 *
 * @author Maxim Butov
 */
public class ReferenceSupplier<T> implements Supplier<T> {

    private final Reference<? extends T> reference;

    public ReferenceSupplier(Reference<? extends T> reference) {
        this.reference = reference;
    }

    @Override
    public T get() {
        return reference.get();
    }

}
