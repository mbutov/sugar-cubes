package org.sugarcubes.builder;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.sugarcubes.function.TernaryConsumer;
import org.sugarcubes.function.TernaryFunction;

/**
 * {@link Builder}-related static helpers.
 *
 * @author Maxim Butov
 */
public class Builders {

    /**
     * Do not use for builder.
     */
    @Deprecated
    public static <T> Builder<T> of(Builder<T> builder) {
        return builder;
    }

    /**
     * Wraps {@link Supplier} with {@link Builder}.
     *
     * @param supplier supplier
     * @return builder
     */
    public static <T> Builder<T> of(Supplier<T> supplier) {
        return supplier instanceof Builder ? (Builder<T>) supplier : supplier::get;
    }

    /**
     * {@link Builder} for constant value.
     *
     * @param value value
     * @return builder
     */
    public static <T> Builder<T> of(T value) {
        return Optional.of(value)::get; // check not null
    }

    /**
     * Creates {@link Function} which takes an object, then calls {@link Consumer#accept(Object)} and then returns
     * the same object.
     *
     * @param consumer consumer
     * @return function
     */
    public static <T> UnaryOperator<T> toIdentity(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }

    /**
     * Creates {@link Consumer} which takes an object, then calls its method with the {@code arg.get()} as an argument.
     *
     * Usage: {@code call(List::add, someObj::getValue)}
     *
     * @param method one-arg method reference
     * @param arg supplier for argument
     * @return consumer
     */
    public static <T, X> Consumer<T> call(BiConsumer<T, X> method, Supplier<X> arg) {
        return t -> method.accept(t, arg.get());
    }

    /**
     * Creates {@link Consumer} which takes an object, then calls its method with the {@code arg} as an argument.
     *
     * Usage: {@code call(List::add, "value")}
     *
     * @param method one-arg method reference
     * @param arg argument
     * @return consumer
     */
    public static <T, X> Consumer<T> call(BiConsumer<T, X> method, X arg) {
        return call(method, of(arg));
    }

    /**
     * Creates {@link Consumer} which takes an object, then calls its method with the {@code arg1.get()} and {@code arg2.get()}
     * as arguments.
     *
     * Usage: {@code call(Map::put, someObj::getKey, someObj::getValue)}
     *
     * @param method two-arg method reference
     * @param arg1 supplier for argument 1
     * @param arg2 supplier for argument 2
     * @return consumer
     */
    public static <T, X, Y> Consumer<T> call(TernaryConsumer<T, X, Y> method, Supplier<X> arg1, Supplier<Y> arg2) {
        return t -> method.accept(t, arg1.get(), arg2.get());
    }

    /**
     * Creates {@link Consumer} which takes an object, then calls its method with the {@code arg1} and {@code arg2}
     * as arguments.
     *
     * Usage: {@code call(Map::put, "key", "value")}
     *
     * @param method two-arg method reference
     * @param arg1 argument 1
     * @param arg2 argument 2
     * @return consumer
     */
    public static <T, X, Y> Consumer<T> call(TernaryConsumer<T, X, Y> method, X arg1, Y arg2) {
        return call(method, of(arg1), of(arg2));
    }

    /**
     * Creates {@link Function} which takes an object, then calls its method with the {@code arg.get()} as an argument and
     * returns the result of this call.
     *
     * Usage: {@code invoke(String::indexOf, person::getName)}
     *
     * @param method one-arg method reference
     * @param arg supplier for argument
     * @return function
     */
    public static <T, X, R> Function<T, R> invoke(BiFunction<T, X, R> method, Supplier<X> arg) {
        return t -> method.apply(t, arg.get());
    }

    /**
     * Creates {@link Function} which takes an object, then calls its method with the {@code arg} as an argument and
     * returns the result of this call.
     *
     * Usage: {@code invoke(String::indexOf, person::getName)}
     *
     * @param method one-arg method reference
     * @param arg supplier for argument
     * @return function
     */
    public static <T, X, R> Function<T, R> invoke(BiFunction<T, X, R> method, X arg) {
        return invoke(method, of(arg));
    }

    /**
     * Creates {@link Function} which takes an object, then calls its method with the {@code arg1.get()} and {@code arg2.get()}
     * as arguments and returns the result of this call.
     *
     * Usage: {@code invoke(String::replace, person::getName, person::getNickname)}
     *
     * @param method two-arg method reference
     * @param arg1 supplier for argument 1
     * @param arg2 supplier for argument 2
     * @return function
     */
    public static <T, X, Y, R> Function<T, R> invoke(TernaryFunction<T, X, Y, R> method, Supplier<X> arg1, Supplier<Y> arg2) {
        return t -> method.apply(t, arg1.get(), arg2.get());
    }

    /**
     * Creates {@link Function} which takes an object, then calls its method with the {@code arg1} and {@code arg2}
     * as arguments and returns the result of this call.
     *
     * Usage: {@code invoke(String::replace, "foo", "bar")}
     *
     * @param method two-arg method reference
     * @param arg1 argument 1
     * @param arg2 argument 2
     * @return function
     */
    public static <T, X, Y, R> Function<T, R> invoke(TernaryFunction<T, X, Y, R> method, X arg1, Y arg2) {
        return invoke(method, of(arg1), of(arg2));
    }

}
