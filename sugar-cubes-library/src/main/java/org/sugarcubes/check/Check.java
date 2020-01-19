package org.sugarcubes.check;

import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.sugarcubes.check.Checks.format;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class Check<E extends Throwable> {

    private final Function<String, E> error;

    public Check(Function<String, E> error) {
        this.error = error;
    }

    public Error fail(Supplier<String> message) throws E {
        throw error.apply(message.get());
    }

    public Error fail(String message, Object... args) throws E {
        throw fail(format(message, args));
    }

    public void check(boolean condition, Supplier<String> message) throws E {
        if (condition) {
            fail(message.get());
        }
    }

    public void check(boolean condition, String message, Object... args) throws E {
        check(condition, format(message, args));
    }

    public void check(BooleanSupplier condition, Supplier<String> message) throws E {
        check(condition.getAsBoolean(), message);
    }

    public void check(BooleanSupplier condition, String message, Object... args) throws E {
        check(condition, format(message, args));
    }

    public <X> X check(X value, Function<X, Boolean> condition, Supplier<String> message) throws E {
        check(condition.apply(value), message);
        return value;
    }

    public <X> X check(X value, Function<X, Boolean> condition, String message, Object... args) throws E {
        return check(value, condition, format(message, args));
    }

    public <X> X notNull(X value, Supplier<String> message) throws E {
        return check(value, Objects::nonNull, message);
    }

    public <X> X notNull(X value, String message, Object... args) throws E {
        return notNull(value, format(message, args));
    }

    public <X> X isNull(X value, Supplier<String> message) throws E {
        return check(value, Objects::isNull, message);
    }

    public <X> X isNull(X value, String message, Object... args) throws E {
        return isNull(value, format(message, args));
    }

}
