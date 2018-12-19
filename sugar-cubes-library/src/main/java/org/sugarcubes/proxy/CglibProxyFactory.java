package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;
import java.util.List;

import org.objenesis.strategy.InstantiatorStrategy;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author Maxim Butov
 */
public class CglibProxyFactory<T> extends InstantiatorProxyFactory<T> {

    private final Class<? extends T> proxyClass;

    @SuppressWarnings("unchecked")
    public CglibProxyFactory(ClassLoader classLoader, GenericType type, InstantiatorStrategy strategy) {
        final Enhancer enhancer = new Enhancer() {
            @Override
            protected void filterConstructors(Class sc, List constructors) {
                // Do not filter: when the class has only private constructor(s), Enhancer doesn't work.
                // But Objenesis still can instantiate objects of such classes.
            }
        };
        enhancer.setClassLoader(classLoader);
        enhancer.setSuperclass(type.getType());
        enhancer.setInterfaces(type.getInterfaces());
        enhancer.setUseFactory(true);
        enhancer.setCallbackType(net.sf.cglib.proxy.InvocationHandler.class);
        enhancer.setSerialVersionUID(1L);
        proxyClass = enhancer.createClass();
        initInstantiator(strategy, proxyClass);
    }

    @Override
    public Class<? extends T> getProxyClass() {
        return proxyClass;
    }

    private static class CglibProxyInvocationHandler extends ProxyInvocationHandler implements net.sf.cglib.proxy.InvocationHandler {

        private static final long serialVersionUID = 1L;

        private CglibProxyInvocationHandler(InvocationHandler handler) {
            super(handler);
        }

    }

    @Override
    protected void initProxy(T proxy, InvocationHandler handler) {
        ((net.sf.cglib.proxy.Factory) proxy)
            .setCallbacks(new net.sf.cglib.proxy.Callback[] {new CglibProxyInvocationHandler(handler)});
    }

}
