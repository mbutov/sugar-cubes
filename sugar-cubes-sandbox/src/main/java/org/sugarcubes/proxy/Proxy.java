package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * Marker interface for own proxies.
 *
 * @author Maxim Butov
 */
public interface Proxy {

    InvocationHandler getHandler();

}
