package org.sugarcubes.check;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Arguments validator.
 *
 * @author Maxim Butov
 */
public class Args {

    public static <T> T check(T arg, Predicate<T> predicate, Supplier<String> message) {
        return Checks.check(arg, predicate, IllegalArgumentException::new, message);
    }

    public static <T> T notNull(T arg, Supplier<String> message) {
        return check(arg, Objects::nonNull, message);
    }

    public static <T> T notNull(T arg, String format, Object... args) {
        return notNull(arg, Checks.format(format, args));
    }

}
