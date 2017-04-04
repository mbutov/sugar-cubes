package org.sugarcubes.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

/**
 * @author Maxim Butov
 */
public class DefaultProxyFactory<T> implements ProxyFactory<T> {

    private final Constructor<? extends T> constructor;

    @SuppressWarnings("unchecked")
    public DefaultProxyFactory(ClassLoader classLoader, Class[] interfaces) {

        Class proxyClass = java.lang.reflect.Proxy.getProxyClass(classLoader, interfaces);
        try {
            constructor = proxyClass.getConstructor(InvocationHandler.class);
        }
        catch (NoSuchMethodException e) {
            throw new ProxyError("No constructor found", e);
        }
        constructor.setAccessible(true);

    }

    @Override
    public Class<? extends T> getProxyClass() {
        return constructor.getDeclaringClass();
    }

    @Override
    public T newProxy(InvocationHandler handler) {
        try {
            return constructor.newInstance(new ProxyInvocationHandler(handler));
        }
        catch (Exception e) {
            throw new ProxyError("Error creating proxy", e);
        }
    }

}
