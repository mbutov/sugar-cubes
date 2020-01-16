package org.sugarcubes.check;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class Check<E extends Throwable> {

    public static final Check<IllegalArgumentException> ARG = new Check<>(IllegalArgumentException::new);
    public static final Check<IllegalStateException> STATE = new Check<>(IllegalStateException::new);

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

    private final Function<String, E> error;

    public Check(Function<String, E> error) {
        this.error = error;
    }

    public void check(BooleanSupplier condition, Supplier<String> message) throws E {
        if (condition.getAsBoolean()) {
            throw error.apply(message.get());
        }
    }

    public <X> X check(X value, Function<X, Boolean> condition, Supplier<String> message) throws E {
        check(() -> condition.apply(value), message);
        return value;
    }

    public <X> X notNull(X value, Supplier<String> message) throws E {
        return check(value, Objects::nonNull, message);
    }

    public <X> X isNull(X value, Supplier<String> message) throws E {
        return check(value, Objects::isNull, message);
    }

}
