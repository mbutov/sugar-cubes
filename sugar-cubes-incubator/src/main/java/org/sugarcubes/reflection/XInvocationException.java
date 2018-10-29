package org.sugarcubes.reflection;

/**
 * Wrapper for checked exception happened during constructor/method execution.
 *
 * @author Maxim Butov
 */
public class XInvocationException extends RuntimeException {

    public XInvocationException(Throwable cause) {
        super(cause);
    }

}
