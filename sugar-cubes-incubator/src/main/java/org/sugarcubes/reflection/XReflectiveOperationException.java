package org.sugarcubes.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * An exception wrapping all {@link ReflectiveOperationException} but {@link InvocationTargetException}.
 *
 * Can be also created without cause exception indicating that XReflection API is not used properly.
 *
 * @author Maxim Butov
 */
public class XReflectiveOperationException extends RuntimeException {

    public XReflectiveOperationException(Throwable cause) {
        super(cause);
    }

    public XReflectiveOperationException(String message) {
        super(message);
    }

    public static Supplier<XReflectiveOperationException> withMessage(String message) {
        return () -> new XReflectiveOperationException(message);
    }

}
