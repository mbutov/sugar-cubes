package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author Maxim Butov
 */
public interface ProxyProvider {

    boolean canProxy(Class clazz);

    <T> ProxyFactory<T> getFactory(GenericType<T> type);

    <T> ProxyFactory<T> getFactory(Class<T> classOrInterface, Class... interfaces);

    boolean isProxy(Object obj);

    InvocationHandler getInvocationHandler(Object proxy);

}
