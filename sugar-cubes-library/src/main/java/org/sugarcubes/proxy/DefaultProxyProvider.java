package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxim Butov
 */
public abstract class DefaultProxyProvider implements ProxyProvider {

    private boolean useJavaProxiesIfPossible;
    private ClassLoader classLoader;

    public DefaultProxyProvider() {
        this(true);
    }

    protected DefaultProxyProvider(boolean useJavaProxiesIfPossible) {
        this.useJavaProxiesIfPossible = useJavaProxiesIfPossible;
    }

    public boolean isUseJavaProxiesIfPossible() {
        return useJavaProxiesIfPossible;
    }

    public void setUseJavaProxiesIfPossible(boolean useJavaProxiesIfPossible) {
        this.useJavaProxiesIfPossible = useJavaProxiesIfPossible;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public boolean canProxy(Class clazz) {
        return GenericType.create(clazz).canBeExtended();
    }

    @Override
    public <T> ProxyFactory<T> getFactory(Class<T> classOrInterface, Class... interfaces) {
        return getFactory(GenericType.create(classOrInterface, interfaces));
    }

    private Map<GenericType, ProxyFactory> cache = new HashMap<GenericType, ProxyFactory>();

    @SuppressWarnings("unchecked")
    public <T> ProxyFactory<T> getFactory(GenericType<T> type) {

        ProxyFactory<T> proxyFactory = cache.get(type);

        if (proxyFactory == null) {
            proxyFactory =
                newProxyFactory(ClassLoaders.getClassLoader(getClassLoader(), getClass()), type.copy().implementing(Proxy.class));
            cache.put(type.unmodifiable(), proxyFactory);
        }

        return proxyFactory;
    }

    @Override
    public boolean isProxy(Object obj) {
        return obj instanceof Proxy;
    }

    @Override
    public InvocationHandler getInvocationHandler(Object proxy) {
        return ((Proxy) proxy).getHandler();
    }

    protected <T> ProxyFactory<T> newProxyFactory(ClassLoader classLoader, GenericType type) {

        if (isUseJavaProxiesIfPossible() && type.isInterfacesOnly()) {
            return newDefaultProxyFactory(classLoader, type.getInterfaces());
        }
        else {
            return newCustomProxyFactory(classLoader, type);
        }

    }

    protected <T> ProxyFactory<T> newCustomProxyFactory(ClassLoader classLoader, GenericType type) {
        throw new IllegalArgumentException("Default implementation supports interfaces only.");
    }

    protected <T> ProxyFactory<T> newDefaultProxyFactory(ClassLoader classLoader, Class[] interfaces) {
        return new DefaultProxyFactory<T>(classLoader, interfaces);
    }

}
