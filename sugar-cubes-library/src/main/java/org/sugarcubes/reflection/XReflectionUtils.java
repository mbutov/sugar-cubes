package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

import org.sugarcubes.rex.Rex;

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

        void run() throws Throwable;

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
            return (Y) callable.call();
        }
        catch (Throwable e) {
            throw Rex.of(e)
                .rethrowIf(InvocationTargetException.class, x -> new XInvocationException(x.getCause()))
                .rethrowIf(ReflectiveOperationException.class, XReflectiveOperationException::new)
                .rethrowAsRuntime();
        }
    }

    static void tryToMakeAccessible(AccessibleObject obj) {
        try {
            obj.setAccessible(true);
        }
        catch (SecurityException e) {
            // ignore
        }
    }

}
