package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class CglibProxyProvider extends InstantiatorProxyProvider {

    public CglibProxyProvider() {
        this(false);
    }

    public CglibProxyProvider(boolean useJavaProxiesIfPossible) {
        super(useJavaProxiesIfPossible);
    }

    @Override
    protected <T> ProxyFactory<T> newCustomProxyFactory(ClassLoader classLoader, GenericType type) {
        return new CglibProxyFactory<T>(classLoader, type, getInstantiatorStrategy());
    }

}
