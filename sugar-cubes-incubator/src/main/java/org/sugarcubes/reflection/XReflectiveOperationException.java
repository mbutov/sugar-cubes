package org.sugarcubes.reflection;

import java.util.function.Supplier;

/**
 * todo: document it and adjust author
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
