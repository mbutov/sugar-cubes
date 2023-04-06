package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;

import org.sugarcubes.executable.XCallable;
import org.sugarcubes.executable.XRunnable;
import org.sugarcubes.rex.Rex;

/**
 * Internal utilities.
 *
 * @author Maxim Butov
 */
class XReflectionUtils {

    static <X, Y extends X> void execute(XRunnable<Throwable> runnable) {
        execute(XCallable.of(runnable));
    }

    static <X, Y extends X> Y execute(XCallable<X, Throwable> callable) {
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

    static <A extends AccessibleObject> A tryToMakeAccessible(A obj) {
        try {
            obj.setAccessible(true);
        }
        catch (SecurityException e) {
            // ignore
        }
        return obj;
    }

}
