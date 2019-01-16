package org.sugarcubes.proxy;

/**
 * General error when working with proxies.
 * Normally must not happen.
 *
 * @author Maxim Butov
 */
public class ProxyError extends Error {

    public ProxyError(String message) {
        super(message);
    }

    public ProxyError(Throwable cause) {
        super(cause);
    }

    public ProxyError(String message, Throwable cause) {
        super(message, cause);
    }

}
