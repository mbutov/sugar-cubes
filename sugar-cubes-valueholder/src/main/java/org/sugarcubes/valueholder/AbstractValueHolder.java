package org.sugarcubes.valueholder;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Абстрактный {@link ValueHolder}.
 *
 * Реализует логику за исключением методов для сохранения и получения хранимого значения - данная логика
 * должна быть реализована в подклассах.
 *
 * @author Maxim Butov
 */
public abstract class AbstractValueHolder<T> implements ValueHolder<T> {

    private Supplier<T> supplier;

    public <V> ValueHolder<V> withInitial(Supplier<V> supplier) {
        if (this.supplier != null) {
            throw new IllegalStateException("withInitial() can be called only once");
        }
        this.supplier = (Supplier) supplier;
        return (ValueHolder<V>) this;
    }

    @Override
    public <V> ValueHolder<V> initialize() {
        create();
        return (ValueHolder) this;
    }

    @Override
    public <V> ValueHolder<V> initialize(V value) {
        set((T) value);
        return (ValueHolder) this;
    }

    @Override
    public T required() {
        T value = get();
        Objects.requireNonNull(value, "Value must not be null");
        return value;
    }

    @Override
    public T create() {
        Objects.requireNonNull(supplier, "Cannot create without supplier");
        remove();
        T value = supplier.get();
        set(value);
        return value;
    }

    @Override
    public T getOrCreate() {
        T value = get();
        return value != null ? value : create();
    }

    @Override
    public T remove() {
        return set(null);
    }

}
