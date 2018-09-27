package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XReflection {

    interface XRunnable {

        void run() throws Exception;

        default Callable<Void> toCallable() {
            return () -> {
                run();
                return null;
            };
        }

    }

    public static void execute(XRunnable runnable) {
        execute(runnable.toCallable());
    }

    public static <X, Y extends X> Y execute(Callable<X> callable) {
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

    public static <T extends AccessibleObject> T prepare(T obj) {
        try {
            obj.setAccessible(true);
        }
        catch (SecurityException e) {
            // ignore
        }
        return obj;
    }

}
