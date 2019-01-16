package org.sugarcubes.arg;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Arguments validator.
 *
 * @author Maxim Butov
 */
public class Arg {

    public static <T> T check(T arg, Predicate<T> predicate, Supplier<String> message) {
        if (!predicate.test(arg)) {
            throw new IllegalArgumentException(message.get());
        }
        return arg;
    }

    public static <T> T notNull(T arg, Supplier<String> message) {
        return check(arg, Objects::nonNull, message);
    }

    public static <T> T notNull(T arg, String format, Object... args) {
        return notNull(arg, format(format, args));
    }

    public static Supplier<String> format(String format, Object... args) {
        return () -> String.format(format, args);
    }

}
