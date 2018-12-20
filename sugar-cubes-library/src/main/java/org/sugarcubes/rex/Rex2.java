package org.sugarcubes.rex;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class Rex2<T extends Throwable> implements Serializable {

    private final T error;

    private Rex2(T error) {
        this.error = error;
    }

    public static <T extends Throwable> Rex2<T> of(T error) {
        return new Rex2<>(error);
    }

    public Error rethrow() throws T {
        throw error;
    }

    public <E extends Throwable> Rex2<E> cast() {
        return (Rex2) this;
    }

    public <E extends Throwable> Rex2<E> cast(Class<E> errorType) {
        if (!is(errorType)) {
            throw new IllegalArgumentException(String.format("%s is not %s", error.getClass(), errorType));
        }
        return cast();
    }

    public <E extends Throwable> boolean is(Class<E> errorType) {
        return errorType.isInstance(error);
    }

    public <E extends Throwable> Rex2<T> rethrowIf(Class<E> errorType) throws E {
        if (is(errorType)) {
            throw cast(errorType).rethrow();
        }
        return this;
    }

    public Rex2<T> rethrowIfRuntime() {
        return rethrowIf(RuntimeException.class);
    }

    public Rex2<T> rethrowIfError() {
        return rethrowIf(Error.class);
    }

    public Rex2<T> rethrowIfUnchecked() {
        return rethrowIfRuntime().rethrowIfError();
    }

    public <E extends Throwable> Rex2<E> replace(Function<T, E> function) {
        return of(function.apply(error));
    }

    public <E extends Throwable> Rex2<T> doIf(Class<E> errorType, Consumer<Rex2<E>> consumer) {
        if (is(errorType)) {
            consumer.accept(cast(errorType));
        }
        return this;
    }

    public <E extends Throwable> Rex2<Throwable> replaceIf(Class<E> errorType, Function<E, Throwable> function) {
        if (is(errorType)) {
            return cast(errorType).replace(function);
        }
        return cast();
    }

}
