package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

import org.objenesis.strategy.InstantiatorStrategy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

/**
 * @author Maxim Butov
 */
public class BytebuddyProxyFactory<T> extends InstantiatorProxyFactory<T> {

    private final Class<? extends T> proxyClass;
    private final DynamicType.Unloaded<? extends T> unloaded;
    private final DynamicType.Loaded<? extends T> loaded;

    @SuppressWarnings("unchecked")
    public BytebuddyProxyFactory(ClassLoader classLoader, GenericType type, InstantiatorStrategy strategy) {
        unloaded = new ByteBuddy()
            .subclass(type.getJavaClass())
            .implement(type.getInterfaces())
            .make();
        loaded = unloaded
            .load(classLoader);
        proxyClass = loaded
            .getLoaded();
        initInstantiator(strategy, proxyClass);
    }

    @Override
    public Class<? extends T> getProxyClass() {
        return proxyClass;
    }

    @Override
    protected void initProxy(T proxy, InvocationHandler handler) {
        //loaded.
    }

}
