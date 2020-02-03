package org.sugarcubes.rex;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

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
public class Rex<T extends Throwable> implements Serializable {

    public static <T extends Throwable> Rex<T> of(T error) {
        return new Rex<>(error);
    }

    private final T error;

    public Rex(T error) {
        this.error = Checks.arg().notNull(error, "Error must be not null.");
    }

    /**
     * Getter.
     *
     * @return the original throwable
     */
    public T getError() {
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
        Throwable p = throwable;
        for (Throwable q; (q = cause(p)) != null; ) {
            p = q;
        }
        return p;
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
    public <E extends Throwable> boolean is(Class<E> errorType) {
        return errorType.isInstance(error);
    }

    /**
     * Casts the Rex wrapper to another type parameter.
     *
     * @return same Rex instance
     */
    public <E extends Throwable> Rex<E> cast() {
        return (Rex) this;
    }

    /**
     * Casts the Rex wrapper to another type parameter if possible.
     *
     * @param errorType error class
     * @return same Rex instance
     * @throws IllegalArgumentException if the original throwable is not instance of {@code errorType}
     */
    public <E extends Throwable> Rex<E> cast(Class<E> errorType) {
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
    public Error rethrow() throws T {
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
    public <E extends Throwable> Error rethrow(Function<T, E> function) throws E {
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

    public <E extends Throwable> Rex<T> rethrowIf(Class<E> errorType) throws E {
        if (is(errorType)) {
            throw this.<E>cast().rethrow();
        }
        return this;
    }

    public <E extends Throwable, X extends Throwable> Rex<T> rethrowIf(Class<E> errorType, Function<E, X> function) throws X {
        if (is(errorType)) {
            throw this.<E>cast().replace(function).rethrow();
        }
        return this;
    }

    public Rex<T> rethrowIfDeclared(Executable executable) throws T {
        Class<Throwable>[] exceptionTypes = (Class[]) executable.getExceptionTypes();
        if (Arrays.stream(exceptionTypes).anyMatch(this::is)) {
            rethrow();
        }
        return this;
    }

    public Rex<T> rethrowIfRuntime() {
        return rethrowIf(RuntimeException.class);
    }

    public Rex<T> rethrowIfError() {
        return rethrowIf(Error.class);
    }

    public Rex<T> rethrowIfUnchecked() {
        return rethrowIfRuntime().rethrowIfError();
    }

    /// replace

    public <E extends Throwable> Rex<E> replace(Function<T, E> function) {
        return of(function.apply(error));
    }

    public <E extends Throwable> Rex<Throwable> replaceIf(Class<E> errorType, Function<E, Throwable> function) {
        if (is(errorType)) {
            return this.<E>cast().replace(function);
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
     *          .submit(e -> logger.error("Unexpected error", e));
     * </pre>
     *
     * @param consumer the action to perform with the throwable
     */
    public Rex<T> submit(Consumer<T> consumer) {
        consumer.accept(error);
        return this;
    }

    /**
     * Submit the throwable into consumer if the throwable has a specified type.
     *
     * @param errorType type of error
     * @param consumer the action to perform with the throwable
     */
    public <E extends Throwable> Rex<T> submitIf(Class<E> errorType, Consumer<E> consumer) {
        if (is(errorType)) {
            this.<E>cast().submit(consumer);
        }
        return this;
    }

}
