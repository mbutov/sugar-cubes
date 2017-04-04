package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class ProxyProviders {

    private static ProxyProviderFactory factory;

    public static void setFactory(ProxyProviderFactory factory) {
        ProxyProviders.factory = factory;
    }

    public static ProxyProviderFactory getFactory() {
        if (factory == null) {

            ProxyProviderFactory[] factories =
                "dalvik".equals(System.getProperty("java.vm.name").toLowerCase()) ?
                    new ProxyProviderFactory[] {ProxyProviderFactory.DEXMAKER, ProxyProviderFactory.JAVA,} :
                    new ProxyProviderFactory[] {ProxyProviderFactory.CGLIB, ProxyProviderFactory.JAVASSIST, ProxyProviderFactory.JAVA,};

            for (ProxyProviderFactory f : factories) {
                if (f.isAvailable(ClassLoaders.getClassLoader())) {
                    factory = f;
                    break;
                }
            }
        }
        return factory;
    }

    public static ProxyProvider newProxyProvider() {
        return newProxyProvider(false);
    }

    public static ProxyProvider newProxyProvider(boolean useJavaProxiesIfPossible) {
        return getFactory().newInstance(useJavaProxiesIfPossible);
    }

}
