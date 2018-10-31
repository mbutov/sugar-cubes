package org.sugarcubes.reflection;

/**
 * Wrapper for exception happened during constructor/method execution.
 *
 * @author Maxim Butov
 */
public class XInvocationException extends XReflectiveOperationException {

    public XInvocationException(Throwable cause) {
        super(cause);
    }

}
