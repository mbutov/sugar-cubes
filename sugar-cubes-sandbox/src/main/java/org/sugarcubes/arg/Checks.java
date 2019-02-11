package org.sugarcubes.arg;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Arguments validator.
 *
 * @author Maxim Butov
 */
public class Checks {

    public static <T> T check(T arg, Predicate<T> predicate, Supplier<RuntimeException> exception) {
        if (!predicate.test(arg)) {
            throw exception.get();
        }
        return arg;
    }

    public static <T> T arg(T arg, Predicate<T> predicate, Supplier<String> message) {
        return check(arg, predicate, () -> new IllegalArgumentException(message.get()));
    }

    public static <T> T arg(T arg, Predicate<T> predicate, String format, Object... args) {
        return arg(arg, predicate, format(format, args));
    }

    public static <T> T argNotNull(T arg, Supplier<String> message) {
        return arg(arg, Objects::nonNull, message);
    }

    public static <T> T argNotNull(T arg, String format, Object... args) {
        return argNotNull(arg, format(format, args));
    }

    public static <T> T state(T arg, Predicate<T> predicate, Supplier<String> message) {
        return check(arg, predicate, () -> new IllegalStateException(message.get()));
    }



    private static Supplier<String> format(String format, Object... args) {
        return () -> String.format(format, args);
    }

}
