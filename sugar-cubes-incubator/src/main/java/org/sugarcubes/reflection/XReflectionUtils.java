package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.stream.Collector;

/**
 * Internal utilities.
 *
 * @author Maxim Butov
 */
class XReflectionUtils {

    interface XCallable<X> {

        X call() throws Throwable;

    }

    interface XRunnable {

        void run() throws Exception;

        default XCallable<Void> toCallable() {
            return () -> {
                run();
                return null;
            };
        }

    }

    static void execute(XRunnable runnable) {
        execute(runnable.toCallable());
    }

    static <X, Y extends X> Y execute(XCallable<X> callable) {
        try {

            try {
                return (Y) callable.call();
            }
            catch (InvocationTargetException e) {
                throw e.getCause();
            }
            catch (ReflectiveOperationException e) {
                throw new XReflectiveOperationException(e);
            }

        }
        catch (RuntimeException | Error e) {
            throw e;
        }
        catch (Throwable e) {
            throw new XInvocationException(e);
        }

    }

    static <T extends AccessibleObject> T tryToMakeAccessible(T obj) {
        try {
            obj.setAccessible(true);
        }
        catch (SecurityException e) {
            // ignore
        }
        return obj;
    }

    private static class CollectorState<X> {

        Optional<X> value;

        void accumulate(X next) {
            if (value == null) {
                value = Optional.ofNullable(next);
            }
            else {
                throw new IllegalStateException("Too many elements");
            }
        }

        CollectorState<X> combine(CollectorState<X> other) {
            if (value != null && other.value != null) {
                throw new IllegalStateException("Too many elements");
            }
            if (value == null) {
                value = other.value;
            }
            return this;
        }

        Optional<X> toOptional() {
            return value != null ? value : Optional.empty();
        }

        X toValue() {
            return toOptional().orElseThrow(() -> new IllegalStateException("No elements"));
        }

    }

    static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional() {
        return Collector.of(CollectorState::new, CollectorState::accumulate, CollectorState::combine, CollectorState::toOptional);
    }

    static <X> Collector<X, Optional<X>, Optional<X>> toOptional2() {
        return Collector.of(Optional::empty,
            (o, x) -> {
                if (o.isPresent()) {
                    throw new IllegalStateException();
                }
            },
            (o1, o2) -> {
                throw new IllegalStateException();
            });
    }

}
