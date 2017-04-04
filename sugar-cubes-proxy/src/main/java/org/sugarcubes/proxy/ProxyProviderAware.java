package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;

/**
 * @author Maxim Butov
 */
public abstract class ProxyProviderAware<P extends ProxyProviderAware> {

    private ProxyProvider proxyProvider;

    protected P setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
        return (P) this;
    }

    protected ProxyProvider getProxyProvider() {
        if (proxyProvider == null) {
            proxyProvider = ProxyProviders.newProxyProvider();
        }
        return proxyProvider;
    }

    protected <T> T newProxy(Class<T> clazz, Class[] interfaces, InvocationHandler invocationHandler) {
        return newProxy(GenericType.create(clazz, interfaces), invocationHandler);
    }

    protected <T> T newProxy(GenericType<T> type, InvocationHandler invocationHandler) {
        return getProxyProvider().getFactory(type).newProxy(invocationHandler);
    }

}
