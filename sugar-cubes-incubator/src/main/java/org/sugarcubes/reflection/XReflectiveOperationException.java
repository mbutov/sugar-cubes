package org.sugarcubes.reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * An exception wrapping all {@link ReflectiveOperationException} but {@link InvocationTargetException}.
 *
 * Can be also created without causing exception indicating that XReflection API is not used properly.
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

    public XReflectiveOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static Supplier<XReflectiveOperationException> withMessage(String message, Object... args) {
        return withMessage(() -> String.format(message, args));
    }

    public static Supplier<XReflectiveOperationException> withMessage(Supplier<String> message) {
        return () -> new XReflectiveOperationException(message.get());
    }

}
