package org.sugarcubes.builder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Class to be extended by custom builders.
 *
 * The difference with simple {@link Builder} is that this class always returns itself when calling {@link #apply(Consumer)}
 * or {@link #replace(Function)}, while other {@link Builder} implementations can return different instance.
 *
 * @author Maxim Butov
 */
public class MutableBuilder<T, B extends MutableBuilder<T, B>> implements Builder<T> {

    /**
     * Builder instance which is changing while calling {@link #apply(Consumer)}.
     */
    private Builder<T> delegate;

    /**
     * Constructor with supplier.
     *
     * @param supplier supplier of initial value
     */
    protected MutableBuilder(Supplier<T> supplier) {
        this.delegate = Builders.of(supplier);
    }

    @Override
    public T build() {
        return delegate.build();
    }

    @Override
    public <V> Builder<V> transform(Function<T, V> function) {
        return delegate.transform(function);
    }

    /**
     * Replaces the builder result using this function.
     *
     * @param function function to apply
     * @return same builder instance
     */
    public B replace(Function<T, T> function) {
        delegate = delegate.transform(function);
        return self();
    }

    @Override
    public B apply(Consumer<T> consumer) {
        delegate = delegate.apply(consumer);
        return self();
    }

    /**
     * Returns itself.
     */
    public B self() {
        return (B) this;
    }

}
