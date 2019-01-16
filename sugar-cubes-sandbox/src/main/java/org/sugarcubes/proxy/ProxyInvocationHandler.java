package org.sugarcubes.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.sugarcubes.rex.Rex;

/**
 * @author Maxim Butov
 */
public class ProxyInvocationHandler extends SmartInvocationHandler implements Serializable, Proxy {

    private static final long serialVersionUID = 1L;

    private final InvocationHandler handler;

    public ProxyInvocationHandler(InvocationHandler handler) {
        interceptMethodsOf(Proxy.class);
        this.handler = handler;
    }

    @Override
    public InvocationHandler getHandler() {
        return handler;
    }

    @Override
    protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.setAccessible(true);
        try {
            return handler.invoke(proxy, method, args);
        }
        catch (Throwable throwable) {
            throw Rex.of(throwable)
                .replaceIf(InvocationTargetException.class, Rex::cause)
                .rethrowIfDeclared(method)
                .rethrowAsRuntime();
        }
    }

}
