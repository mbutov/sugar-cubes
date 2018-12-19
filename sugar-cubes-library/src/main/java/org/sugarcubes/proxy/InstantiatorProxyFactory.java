package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

import org.objenesis.instantiator.ObjectInstantiator;
import org.objenesis.strategy.InstantiatorStrategy;

/**
 * @author Maxim Butov
 */
public abstract class InstantiatorProxyFactory<T> implements ProxyFactory<T> {

    private ObjectInstantiator instantiator;

    protected void initInstantiator(InstantiatorStrategy strategy, Class<? extends T> type) {
        instantiator = strategy.newInstantiatorOf(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T newProxy(InvocationHandler handler) {
        T proxy = (T) instantiator.newInstance();
        initProxy(proxy, handler);
        return proxy;
    }

    protected abstract void initProxy(T proxy, InvocationHandler handler);
}
