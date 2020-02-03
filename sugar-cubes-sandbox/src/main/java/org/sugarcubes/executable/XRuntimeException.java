package org.sugarcubes.executable;

/**
 * A wrapper for checked exception.
 *
 * @author Maxim Butov
 */
public class XRuntimeException extends RuntimeException{

    public XRuntimeException(Throwable cause) {
        super(cause);
    }

}
