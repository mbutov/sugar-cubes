package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.objenesis.strategy.InstantiatorStrategy;

/**
 * @author Maxim Butov
 */
public class JavassistProxyFactory<T> extends InstantiatorProxyFactory<T> {

    private final Class<? extends T> proxyClass;

    @SuppressWarnings("unchecked")
    public JavassistProxyFactory(final ClassLoader classLoader, GenericType type, InstantiatorStrategy strategy) {
        javassist.util.proxy.ProxyFactory proxyFactory = new javassist.util.proxy.ProxyFactory() {
            @Override
            protected ClassLoader getClassLoader() {
                return classLoader;
            }
        };
        proxyFactory.setSuperclass(type.getType());
        proxyFactory.setInterfaces(type.getInterfaces());
        proxyClass = proxyFactory.createClass();
        initInstantiator(strategy, proxyClass);
    }

    @Override
    public Class<? extends T> getProxyClass() {
        return proxyClass;
    }

    private static class JavassistProxyInvocationHandler extends ProxyInvocationHandler implements javassist.util.proxy.MethodHandler {

        private static final long serialVersionUID = 1L;

        private JavassistProxyInvocationHandler(InvocationHandler handler) {
            super(handler);
        }

        @Override
        public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
            return invoke(self, thisMethod, args);
        }

    }

    @Override
    protected void initProxy(T proxy, InvocationHandler handler) {
        ((javassist.util.proxy.Proxy) proxy).setHandler(new JavassistProxyInvocationHandler(handler));
    }

}
