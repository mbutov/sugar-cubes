package org.sugarcubes.proxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import org.objenesis.strategy.InstantiatorStrategy;

import com.google.dexmaker.stock.ProxyBuilder;

/**
 * @author Maxim Butov
 */
@SuppressWarnings("unchecked")
public class DexmakerProxyFactory<T> extends InstantiatorProxyFactory<T> {

    private static final String FIELD_NAME_HANDLER = "$__handler";

    private final Field handlerField;

    public DexmakerProxyFactory(ClassLoader classLoader, File dexCache, GenericType type, InstantiatorStrategy strategy) {

        ProxyBuilder<T> builder = ProxyBuilder.<T>forClass(type.getType()).implementing(type.getInterfaces())
            .parentClassLoader(classLoader).dexCache(dexCache);

        Class<? extends T> proxyClass;
        try {
            proxyClass = builder.buildProxyClass();
        }
        catch (IOException e) {
            throw new ProxyError("Error creating proxy class", e);
        }

        initInstantiator(strategy, proxyClass);

        try {
            handlerField = proxyClass.getDeclaredField(FIELD_NAME_HANDLER);
        }
        catch (NoSuchFieldException e) {
            throw new ProxyError("Can't get handler field", e);
        }
        handlerField.setAccessible(true);
    }

    @Override
    public Class<? extends T> getProxyClass() {
        return (Class) handlerField.getDeclaringClass();
    }

    @Override
    protected void initProxy(T proxy, InvocationHandler handler) {
        try {
            handlerField.set(proxy, new ProxyInvocationHandler(handler));
        }
        catch (IllegalAccessException e) {
            throw new ProxyError("Can't set handler", e);
        }
    }
}
