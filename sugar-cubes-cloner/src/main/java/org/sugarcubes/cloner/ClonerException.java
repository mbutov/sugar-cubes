package org.sugarcubes.cloner;

/**
 * Generic cloner exception. Wraps some happened checked exception.
 *
 * @author Maxim Butov
 */
public class ClonerException extends RuntimeException {

    public ClonerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClonerException(Throwable cause) {
        super(cause);
    }

}
