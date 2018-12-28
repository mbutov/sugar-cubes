package org.sugarcubes.builder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.sugarcubes.function.Invocations;

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
     * @param function function to apply
     * @return new builder
     */
    default <V> Builder<V> transform(Function<T, V> function) {
        return () -> function.apply(get());
    }

    /**
     * Shortcut {@code for transform(function).get()}.
     *
     * @param function function to apply
     * @return the final value
     */
    default <V> V get(Function<T, V> function) {
        return transform(function).get();
    }

    /**
     * Calls {@link Consumer#accept(Object)} to the object before returning it from {@link #get()}.
     *
     * @param consumer consumer
     * @return new builder instance, for the same instance use {@link MutableBuilder}
     */
    default Builder<T> apply(Consumer<T> consumer) {
        return transform(Invocations.toIdentity(consumer));
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
