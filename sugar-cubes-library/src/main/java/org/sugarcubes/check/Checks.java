package org.sugarcubes.check;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Arguments validator.
 *
 * @author Maxim Butov
 */
public class Checks {

    public static Supplier<String> format(String format) {
        return () -> format;
    }

    public static Supplier<String> format(String format, Object... args) {
        return () -> String.format(format, args);
    }

    public static <T> T check(T arg, Predicate<T> predicate, Supplier<RuntimeException> exception) {
        if (!predicate.test(arg)) {
            throw exception.get();
        }
        return arg;
    }

    public static <T> T check(T arg, Predicate<T> predicate, Function<String, RuntimeException> error, Supplier<String> message) {
        return check(arg, predicate, () -> error.apply(message.get()));
    }

}
