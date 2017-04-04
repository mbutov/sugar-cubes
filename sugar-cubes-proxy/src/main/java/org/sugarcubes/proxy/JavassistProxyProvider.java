package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class JavassistProxyProvider extends InstantiatorProxyProvider {

    public JavassistProxyProvider() {
        this(false);
    }

    public JavassistProxyProvider(boolean useJavaProxiesIfPossible) {
        super(useJavaProxiesIfPossible);
    }

    @Override
    protected <T> ProxyFactory<T> newCustomProxyFactory(ClassLoader classLoader, GenericType type) {
        return new JavassistProxyFactory<T>(classLoader, type, getInstantiatorStrategy());
    }

}
