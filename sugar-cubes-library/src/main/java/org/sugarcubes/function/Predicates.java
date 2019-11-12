package org.sugarcubes.function;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import static java.util.stream.Stream.of;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class Predicates {

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T> first, Predicate<T> second, Predicate<T>... rest) {
        return reduce(Predicate::and, first, second, rest);
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<T> first, Predicate<T> second, Predicate<T>... rest) {
        return reduce(Predicate::or, first, second, rest);
    }

    private static <X> X reduce(BinaryOperator<X> accumulator, X first, X second, X[] rest) {
        return of(rest).reduce(accumulator.apply(first, second), accumulator);
    }

    public static <T> Predicate<T> xor(Predicate<T> first, Predicate<T> second) {
        return (T t) -> first.test(t) ^ second.test(t);
    }

}
