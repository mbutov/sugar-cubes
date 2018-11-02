package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

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
        catch (InvocationTargetException e) {
            throw new XInvocationException(e.getCause());
        }
        catch (ReflectiveOperationException e) {
            throw new XReflectiveOperationException(e);
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable e) {
            throw new XReflectiveOperationException("Unexpected throwable", e);
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

}
