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

    public static Supplier<RuntimeException> withMessage(Function<String, RuntimeException> error, Supplier<String> message) {
        return () -> error.apply(message.get());
    }

    public static void check(boolean condition, Supplier<RuntimeException> error) {
        if (!condition) {
            throw error.get();
        }
    }

    public static void check(boolean condition, Function<String, RuntimeException> error, Supplier<String> message) {
        check(condition, withMessage(error, message));
    }

    public static <T> T check(T arg, Predicate<T> predicate, Supplier<RuntimeException> error) {
        check(predicate.test(arg), error);
        return arg;
    }

    public static <T> T check(T arg, Predicate<T> predicate, Function<String, RuntimeException> error, Supplier<String> message) {
        return check(arg, predicate, withMessage(error, message));
    }

}
