package org.sugarcubes.rex;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.sugarcubes.check.Checks;

/**
 * Rex means "runtime exceptions".
 *
 * The wrapper around {@link Throwable} adding some handy operations.
 *
 * The typical use-case is:
 *
 * <pre>
 *      try {
 *          Method foo = myObj.getClass().getDeclaredMethod("foo", Object.class);
 *          Object result = foo.invoke(myObj, bar);
 *          doSmthWthResult(result);
 *      }
 *      catch (Exception e) {
 *          throw Rex.of(e)
 *              .apply(ex -> logger.error("Some error happened", ex))
 *              .rethrowIfUnchecked()
 *              .replaceIf(InvocationTargetException.class, Throwable::getCause)
 *              .rethrowIf(SomeSpecificException.class)
 *              .rethrowAsRuntime();
 *      }
 * </pre>
 *
 * In the catch block we have the following:
 *
 * <ol>
 *     <li>creating Rex wrapper on exception</li>
 *     <li>if the exception is unchecked (Error or RuntimeException), then rethrow it</li>
 *     <li>if the exception is InvocationTargetException then replacing it with its cause</li>
 *     <li>if the current exception is SomeSpecificException then throw it</li>
 *     <li>rethrow as runtime, it means wrap it with RuntimeException and only then throw</li>
 * </ol>
 *
 * @author Maxim Butov
 */
public class Rex<E extends Throwable> implements Serializable {

    public static <E extends Throwable> Rex<E> of(E error) {
        return new Rex<>(error);
    }

    private final E error;

    private Rex(E error) {
        this.error = Checks.arg().notNull(error, "Error must be not null.");
    }

    /**
     * Getter.
     *
     * @return the original throwable
     */
    public E getError() {
        return error;
    }

    /// static helper methods

    public static Throwable cause(Throwable throwable) {
        return throwable.getCause();
    }

    public static Throwable causeOrSelf(Throwable throwable) {
        Throwable cause = cause(throwable);
        return cause != null ? cause : throwable;
    }

    public static Throwable root(Throwable throwable) {
        return Stream.iterate(throwable, Objects::nonNull, Throwable::getCause)
            .reduce((p, q) -> q)
            .orElseThrow();
    }

    /**
     * Prints stacktrace into memory buffer and returns it as string.
     *
     * @param throwable error
     *
     * @return stacktrace
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter buffer = new StringWriter(0x0100); // 4K buffer
        throwable.printStackTrace(new PrintWriter(buffer, true));
        return buffer.toString();
    }

    public static <X extends Throwable, Y extends Throwable> Function<X, Y> withMessage(String message,
        BiFunction<String, X, Y> messageCauseConstructor) {
        return x -> messageCauseConstructor.apply(message, x);
    }

    /// is-cast

    /**
     * Checks whether the original throwable is of this type.
     *
     * @param errorType type to check
     * @return true if the original throwable is of this type
     */
    public <X extends Throwable> boolean is(Class<X> errorType) {
        return errorType.isInstance(error);
    }

    /**
     * Casts the Rex wrapper to another type parameter.
     *
     * @return same Rex instance
     */
    public <X extends Throwable> Rex<X> cast() {
        return (Rex) this;
    }

    /**
     * Casts the Rex wrapper to another type parameter if possible.
     *
     * @param errorType error class
     * @return same Rex instance
     * @throws IllegalArgumentException if the original throwable is not instance of {@code errorType}
     */
    public <X extends Throwable> Rex<X> cast(Class<X> errorType) {
        if (!is(errorType)) {
            throw new IllegalArgumentException(String.format("%s is not %s", error.getClass(), errorType));
        }
        return cast();
    }

    /// rethrow

    /**
     * Rethrows the original throwable.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwUndeclared();}" and tell compiler that execution will not go further.
     */
    public Error rethrow() throws E {
        throw error;
    }

    /**
     * Converts the original throwable into the new one and throws it.
     *
     * Same as {@code replace(function).rethrow()}.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwUndeclared();}" and tell compiler that execution will not go further.
     */
    public <X extends Throwable> Error rethrow(Function<E, X> function) throws X {
        return replace(function).rethrow();
    }

    /**
     * Rethrows the original throwable inside of method which does not declare this type of throwable.
     * Uses a kind of <a href="http://stackoverflow.com/a/18408831">this</a> trick.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwUndeclared();}" and tell compiler that execution will not go further.
     */
    public Error rethrowUndeclared() {
        throw this.<RuntimeException>cast().rethrow();
    }

    public Error rethrowAsRuntime() {
        return rethrowIfUnchecked().replace(RuntimeException::new).rethrow();
    }

    public static Error rethrowAsRuntime(Throwable error) {
        return of(error).rethrowAsRuntime();
    }

    public <X extends Throwable> Rex<E> rethrowIf(Class<X> errorType) throws X {
        if (is(errorType)) {
            throw this.<X>cast().rethrow();
        }
        return this;
    }

    public <X extends Throwable, Y extends Throwable> Rex<E> rethrowIf(Class<X> errorType, Function<X, Y> function) throws Y {
        if (is(errorType)) {
            throw this.<X>cast().replace(function).rethrow();
        }
        return this;
    }

    public Rex<E> rethrowIfDeclared(Executable executable) throws E {
        Class<Throwable>[] exceptionTypes = (Class[]) executable.getExceptionTypes();
        if (Arrays.stream(exceptionTypes).anyMatch(this::is)) {
            rethrow();
        }
        return this;
    }

    public Rex<E> rethrowIfRuntime() {
        return rethrowIf(RuntimeException.class);
    }

    public Rex<E> rethrowIfError() {
        return rethrowIf(Error.class);
    }

    public Rex<E> rethrowIfUnchecked() {
        return rethrowIfRuntime().rethrowIfError();
    }

    /// replace

    public <X extends Throwable> Rex<X> replace(Function<E, X> function) {
        return of(function.apply(error));
    }

    public <X extends Throwable> Rex<Throwable> replaceIf(Class<X> errorType, Function<X, Throwable> function) {
        if (is(errorType)) {
            return this.<X>cast().replace(function);
        }
        return cast();
    }

    /// apply

    /**
     * Submit the throwable into consumer.
     *
     * Example:
     *
     * <pre>
     *      Rex.of(e)
     *          .throwIfUnchecked()
     *          .throwIf(IOException.class)
     *          .throwIf(ServletException.class)
     *          .peek(e -> logger.error("Unexpected error", e));
     * </pre>
     *
     * @param consumer the action to perform with the throwable
     */
    public Rex<E> peek(Consumer<E> consumer) {
        consumer.accept(error);
        return this;
    }

    /**
     * Submit the throwable into consumer if the throwable has a specified type.
     *
     * @param errorType type of error
     * @param consumer the action to perform with the throwable
     */
    public <X extends Throwable> Rex<E> peekIf(Class<X> errorType, Consumer<X> consumer) {
        if (is(errorType)) {
            this.<X>cast().peek(consumer);
        }
        return this;
    }

}
