package org.sugarcubes.rex;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
 *              .replaceWithCauseIf(InvocationTargetException.class)
 *              .throwIf(SomeSpecificException.class)
 *              .throwUnchecked();
 *      }
 * </pre>
 *
 * In the catch block we have the following:
 *
 * <ol>
 *     <li>line: creating Rex wrapper on exception</li>
 *     <li>line: if the exception is InvocationTargetException then replacing it with its cause</li>
 *     <li>line: if the current exception is SomeSpecificException then throw it</li>
 *     <li>line: throw unchecked, it means if the exception is unchecked, then thrown it, if checked, then wrap it with
 *     RuntimeException and only then throw</li>
 * </ol>
 *
 * @author Maxim Butov
 */
public class Rex<T extends Throwable> {

    /**
     * Static factory-method for creating {@link Rex} instance wrapping a {@link Throwable}.
     *
     * @param throwable the throwable happened
     * @return Rex wrapper
     */
    public static <T extends Throwable> Rex<T> of(T throwable) {
        return new Rex<>(throwable);
    }

    /**
     * Static factory-method for creating {@link Rex} instance wrapping a {@link Throwable}.
     *
     * @param throwable the throwable happened
     * @param translator function which creates an unchecked exception for a checked one
     * @return Rex wrapper
     */
    public static <T extends Throwable> Rex<T> of(T throwable, Function<Throwable, RuntimeException> translator) {
        return new Rex<>(throwable, translator);
    }

    /**
     * A shortcut for the most often used use-case: catch an exception, rethrow it if it is unchecked,
     * or, if it is checked, wrap it with RuntimeException and then rethrow.
     *
     * @param throwable the throwable happened
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw Rex.throwUnchecked(e);}" and tell compiler that execution will not go further.
     */
    public static Error throwUnchecked(Throwable throwable) {
        return of(throwable).throwUnchecked();
    }

    /**
     * Executes callable and wraps checked exception.
     *
     * @param callable callable
     * @return result of {@code callable.call()}
     * @throws RuntimeException if callable executes with error
     */
    public static <T> T call(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw Rex.throwUnchecked(e);
        }
    }

    /**
     * The original throwable.
     */
    private final T throwable;

    /**
     * Function converting checked exception into unchecked.
     */
    private final Function<Throwable, RuntimeException> translator;

    /**
     * Constructor.
     *
     * @param throwable the original throwable
     * @param translator function converting checked exception into unchecked
     */
    public Rex(T throwable, Function<Throwable, RuntimeException> translator) {
        Objects.requireNonNull(throwable);
        Objects.requireNonNull(translator);
        this.throwable = throwable;
        this.translator = translator;
    }

    /**
     * Constructor.
     *
     * @param throwable the original throwable
     */
    public Rex(T throwable) {
        this(throwable, Extra.DEFAULT);
    }

    /**
     * Getter.
     *
     * @return the original throwable
     */
    public T getThrowable() {
        return throwable;
    }

    /**
     * Converts the original throwable into {@link RuntimeException}.
     * Method IS NOT SUPPOSED to throw any exception.
     *
     * @return unchecked exception
     */
    protected RuntimeException wrapWithRuntime() {
        return translator.apply(throwable);
    }

    /**
     * Rethrows the original throwable.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.rethrow();}" and tell compiler that execution will not go further.
     */
    public Error rethrow() throws T {
        throw throwable;
    }

    /**
     * Rethrows the original throwable inside of method which does not declare this type of throwable.
     * Uses a kind of <a href="http://stackoverflow.com/a/18408831">this</a> trick.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwUndeclared();}" and tell compiler that execution will not go further.
     */
    public Error throwUndeclared() {
        return this.<RuntimeException>cast().rethrow();
    }

    /**
     * Rethrows the original throwable if it is unchecked, or wraps it with unchecked exception
     * and then throws the unchecked one.
     *
     * @see #throwIfUnchecked()
     * @see #wrapWithRuntime()
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwUnchecked();}" and tell compiler that execution will not go further.
     */
    public Error throwUnchecked() {
        throw throwIfUnchecked().wrapWithRuntime();
    }

    /**
     * Rethrows the new throwable instead of original.
     *
     * The recommended usage is: {@code throw Rex.of(e).throwOther(MyException::new); }.
     *
     * @return never returns result, the return type {@link Error} is needed to write code like
     * "{@code throw rex.throwOther(MyException::new);}" and tell compiler that execution will not go further.
     */
    public <E extends Throwable> Error throwOther(Function<T, E> function) throws E {
        throw function.apply(throwable);
    }

    /**
     * Checks whether the original throwable is of this type.
     *
     * @param exceptionClass type to check
     * @return true if the original throwable is of this type
     */
    public boolean isOf(Class<? extends Throwable> exceptionClass) {
        return exceptionClass.isInstance(throwable);
    }

    /**
     * Checks whether the original throwable is of one of these types.
     *
     * @param exceptionClasses classes to check
     * @return true if the original throwable is of one of these types
     */
    public boolean isAnyOf(Class<? extends Throwable>... exceptionClasses) {
        return Arrays.stream(exceptionClasses).anyMatch(this::isOf);
    }

    /**
     * Rethrows the original throwable if it is of specified type.
     *
     * @param exceptionClass exception type to check
     * @return same Rex instance
     * @throws E original throwable casted to type {@code exceptionClass}
     */
    public <E extends Throwable> Rex<T> throwIf(Class<E> exceptionClass) throws E {
        if (isOf(exceptionClass)) {
            throw (E) throwable;
        }
        return this;
    }

    /**
     * Rethrows the original throwable if is of one of specified types.
     *
     * @param exceptionClasses exception types to check
     * @return same Rex instance
     * @throws T original throwable
     */
    public Rex<T> throwIf(Class<Throwable>... exceptionClasses) throws T {
        if (isAnyOf(exceptionClasses)) {
            throw rethrow();
        }
        return this;
    }

    /**
     * Rethrows the original throwable if is of one of declared in the method or in the constructor
     * passed.
     *
     * @param methodOrConstructor method or constructor
     * @return same Rex instance
     * @throws T original throwable
     */
    public Rex<T> throwIf(Executable methodOrConstructor) throws T {
        return throwIf((Class<Throwable>[]) methodOrConstructor.getExceptionTypes());
    }

    /**
     * Throws the original throwable if it is {@link Error}.
     *
     * @return same Rex instance
     * @throws Error the original throwable casted to {@link Error}
     */
    public Rex<T> throwIfError() throws Error {
        return throwIf(Error.class);
    }

    /**
     * Throws the original throwable if it is {@link RuntimeException}.
     *
     * @return same Rex instance
     * @throws RuntimeException the original throwable casted to {@link RuntimeException}
     */
    public Rex<T> throwIfRuntime() throws RuntimeException {
        return throwIf(RuntimeException.class);
    }

    /**
     * Throws the original throwable if it is {@link Error} or {@link RuntimeException}.
     *
     * @return same Rex instance
     * @throws Error the original throwable casted to {@link Error}
     * @throws RuntimeException the original throwable casted to {@link RuntimeException}
     */
    public Rex<T> throwIfUnchecked() throws Error, RuntimeException {
        return throwIfRuntime().throwIfError();
    }

    /**
     * Creates Rex-wrapper for the cause throwable.
     *
     * @return Rex-wrapper for the cause throwable or {@code null} if there is no cause
     */
    public Rex<Throwable> cause() {
        Throwable cause = throwable.getCause();
        return cause != null ? new Rex<>(cause, translator) : null;
    }

    /**
     * Creates Rex-wrapper for the root cause.
     *
     * @return Rex-wrapper for the root cause, may be the same Rex instance
     */
    public Rex<Throwable> root() {
        Throwable p = throwable;
        for (Throwable q; (q = p.getCause()) != null; ) {
            p = q;
        }
        return p != throwable ? new Rex<>(p) : cast();
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
     * Creates new Rex-wrapper for cause if the original throwable is of specified type.
     * This use-case is useful for example for {@link InvocationTargetException}.
     *
     * @param exceptionClass type to check
     * @return Rex-wrapper of cause or the same instance
     */
    public <E extends Throwable> Rex<Throwable> replaceWithCauseIf(Class<E> exceptionClass) {
        return isOf(exceptionClass) ? cause() : cast();
    }

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
        consumer.accept(throwable);
        return this;
    }

    /**
     * Prints stacktrace into memory buffer and returns it as string.
     *
     * @return string with stacktrace
     */
    public String getStackTrace() {
        StringWriter buffer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(buffer, true));
        return buffer.toString();
    }

    public <E extends Throwable> Rex<T> ifThenThrow(Predicate<Throwable> predicate, Function<Throwable, E> function) throws E {
        if (predicate.test(throwable)) {
            throw function.apply(throwable);
        }
        return this;
    }

    public <X extends Throwable, Y extends Throwable> Rex<T> ifThenThrow(Class<X> type, Function<X, Y> function) throws Y {
        return ifThenThrow(type::isInstance, e -> function.apply((X) e));
    }
}
