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

        boolean isSet;
        X value;

        void accumulate(X next) {
            if (isSet) {
                throw new IllegalStateException("Too many elements");
            }
            isSet = true;
            value = next;
        }

        CollectorState<X> combine(CollectorState<X> other) {
            if (isSet && other.isSet) {
                throw new IllegalStateException("Too many elements");
            }
            return isSet ? this : other;
        }

        Optional<X> toOptional() {
            return Optional.ofNullable(value);
        }

        X toOnlyElement() {
            if (!isSet) {
                throw new IllegalStateException("No elements");
            }
            return value;
        }

    }

    static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional() {
        return Collector.of(CollectorState::new, CollectorState::accumulate, CollectorState::combine, CollectorState::toOptional,
            Collector.Characteristics.UNORDERED);
    }

    static <X> Collector<X, CollectorState<X>, X> toOnlyElement() {
        return Collector.of(CollectorState::new, CollectorState::accumulate, CollectorState::combine, CollectorState::toOnlyElement,
            Collector.Characteristics.UNORDERED);
    }

}
