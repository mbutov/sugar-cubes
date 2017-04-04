package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class BytebuddyProxyProvider extends InstantiatorProxyProvider {

    public BytebuddyProxyProvider() {
        this(false);
    }

    public BytebuddyProxyProvider(boolean useJavaProxiesIfPossible) {
        super(useJavaProxiesIfPossible);
    }

    @Override
    protected <T> ProxyFactory<T> newCustomProxyFactory(ClassLoader classLoader, GenericType type) {
        return new CglibProxyFactory<T>(classLoader, type, getInstantiatorStrategy());
    }

}
