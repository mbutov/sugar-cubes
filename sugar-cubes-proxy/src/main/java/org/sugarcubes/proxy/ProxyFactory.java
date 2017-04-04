package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author Maxim Butov
 */
public interface ProxyFactory<T> {

    Class<? extends T> getProxyClass();

    T newProxy(InvocationHandler handler);

}
