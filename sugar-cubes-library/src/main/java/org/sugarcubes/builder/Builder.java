package org.sugarcubes.builder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Generic builder for any objects. Almost same as {@link Supplier}, just have methods
 * {@link #transform(Function)} and {@link #apply(Consumer)}.
 *
 * @author Maxim Butov
 */
public interface Builder<T> extends Supplier<T> {

    /**
     * Returns new builder, which applies function to the result of this builder.
     *
     * @param function function to aplly
     * @return new builder
     */
    default <V> Builder<V> transform(Function<T, V> function) {
        return () -> function.apply(get());
    }

    /**
     * Calls {@link Consumer#accept(Object)} to the object before returning it from {@link #get()}.
     *
     * @param consumer consumer
     * @return new builder instance, for the same instance use {@link MutableBuilder}
     */
    default Builder<T> apply(Consumer<T> consumer) {
        return transform(Builders.consumerToFunction(consumer));
    }

    /**
     * Calls {@link Consumer#accept(Object)} to the object before returning it from {@link #get()}.
     *
     * @param consumer consumer
     * @return new builder instance, for the same instance use {@link MutableBuilder}
     */
    default <X> Builder<T> apply(BiConsumer<T, X> method, X arg) {
        return transform(Builders.consumerToFunction(value -> method.accept(value, arg)));
    }

    /**
     * Completes the build process. Replaces this builder with supplier to avoid further modification.
     *
     * @return supplier
     */
    default Supplier<T> complete() {
        return this::get;
    }

    /**
     * Returns itself.
     */
    default Builder<T> self() {
        return this;
    }

}
