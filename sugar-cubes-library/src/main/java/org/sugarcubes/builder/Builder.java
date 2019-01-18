package org.sugarcubes.builder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.sugarcubes.function.Invocations;

/**
 * <p>Generic builder for any objects.</p>
 * <p></p>
 * <p>The scenario of usage is:</p>
 * <ol>
 *     <li>Create a builder using {@link Builders#of(Supplier)}</li>
 *     <li>Modify its state with {@link #transform(Function)} and {@link #apply(Consumer)} methods</li>
 *     <li>Create an object with {@link #build()}</li>
 * </ol>
 * <p></p>
 * <p>Example</p>
 * <pre>{@code
 *      Map<String, Integer> map = Builders.<Map<String, Integer>>of(HashMap::new)
 *          .apply(Invocations.call(Map::put, "teeth", 32))
 *          .apply(Invocations.call(Map::put, "fingers", 8))
 *          .transform(Collections::unmodifiableMap)
 *          .build();
 * }</pre>
 * @author Maxim Butov
 */
@FunctionalInterface
public interface Builder<T> {

    /**
     * Creates an object from seed, applies all modifications and returns the final object
     *
     * @return built object
     */
    T build();

    /**
     * Returns new builder, which applies function to the result of this builder.
     *
     * @param function function to apply
     *
     * @return new builder instance
     */
    default <V> Builder<V> transform(Function<T, V> function) {
        return () -> function.apply(build());
    }

    /**
     * Calls {@link Consumer#accept(Object)} to the object before returning it from {@link #build()}.
     *
     * @param consumer consumer
     *
     * @return new builder instance, for the same instance use {@link MutableBuilder}
     */
    default Builder<T> apply(Consumer<T> consumer) {
        return transform(Invocations.toIdentity(consumer));
    }

}
