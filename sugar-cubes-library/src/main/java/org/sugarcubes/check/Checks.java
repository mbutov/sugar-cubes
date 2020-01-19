package org.sugarcubes.check;

import java.util.function.Supplier;

/**
 * {@link Check}s factory and helper methods.
 *
 * @author Maxim Butov
 */
public class Checks {

    private static final Check<IllegalArgumentException> ARG = new Check<>(IllegalArgumentException::new);

    private static final Check<IllegalStateException> STATE = new Check<>(IllegalStateException::new);

    public static Check<IllegalArgumentException> arg() {
        return ARG;
    }

    public static Check<IllegalStateException> state() {
        return STATE;
    }

    public static Supplier<String> format(String format) {
        return () -> format;
    }

    public static Supplier<String> format(String format, Object... args) {
        return args.length > 0 ? () -> String.format(format, args) : format(format);
    }

}
