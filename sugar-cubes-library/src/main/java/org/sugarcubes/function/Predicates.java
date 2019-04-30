package org.sugarcubes.function;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class Predicates {

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

    public static <T> Predicate<T> and(Predicate<T> first, Predicate<T> second, Predicate<T>... rest) {
        return Stream.of(rest).reduce(first.and(second), Predicate::and);
    }

    public static <T> Predicate<T> or(Predicate<T> first, Predicate<T> second, Predicate<T>... rest) {
        return Stream.of(rest).reduce(first.or(second), Predicate::or);
    }

    public static <T> Predicate<T> xor(Predicate<T> first, Predicate<T> second) {
        return (T t) -> first.test(t) ^ second.test(t);
    }

}
