package org.sugarcubes.proxy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Maxim Butov
 */
public abstract class ProxyProviderFactory {

    public static final ProxyProviderFactory JAVA = new ProxyProviderFactory(null, null) {

        @Override
        public boolean isAvailable(ClassLoader classLoader) {
            return true;
        }

        @Override
        public boolean isProxyClass(Class clazz) {
            return java.lang.reflect.Proxy.isProxyClass(clazz);
        }

        @Override
        public ProxyProvider newInstance(boolean useJavaProxiesIfPossible) {
            return new JavaProxyProvider();
        }

    };

    public static final ProxyProviderFactory CGLIB = new ProxyProviderFactory("net.sf.cglib.proxy.Enhancer", "isEnhanced") {

        @Override
        public ProxyProvider newInstance(boolean useJavaProxiesIfPossible) {
            return new CglibProxyProvider(useJavaProxiesIfPossible);
        }

    };

    public static final ProxyProviderFactory JAVASSIST = new ProxyProviderFactory("javassist.util.proxy.ProxyFactory", "isProxyClass") {

        @Override
        public ProxyProvider newInstance(boolean useJavaProxiesIfPossible) {
            return new JavassistProxyProvider(useJavaProxiesIfPossible);
        }

    };

    public static final ProxyProviderFactory DEXMAKER = new ProxyProviderFactory("com.google.dexmaker.stock.ProxyBuilder", "isProxyClass") {

        @Override
        public ProxyProvider newInstance(boolean useJavaProxiesIfPossible) {
            return new DexmakerProxyProvider(useJavaProxiesIfPossible);
        }

    };

    public static final List<ProxyProviderFactory> FACTORIES = Collections.unmodifiableList(Arrays.asList(JAVA, CGLIB, JAVASSIST, DEXMAKER));

    private final String factoryClassName;
    private final String isProxyMethodName;

    protected ProxyProviderFactory(String factoryClassName, String isProxyMethodName) {
        this.factoryClassName = factoryClassName;
        this.isProxyMethodName = isProxyMethodName;
    }

    public boolean isAvailable(ClassLoader classLoader) {
        try {
            Class.forName(factoryClassName, true, ClassLoaders.getClassLoader(classLoader));
            return true;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }

    public boolean isProxyClass(Class clazz) {
        Class factoryClass;
        try {
            factoryClass = Class.forName(factoryClassName, true, ClassLoaders.getClassLoader(clazz.getClassLoader()));
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        try {
            return (Boolean) factoryClass.getMethod(isProxyMethodName, Class.class).invoke(null, clazz);
        }
        catch (Exception e) {
            throw new ProxyError(e);
        }
    }

    public ProxyProvider newInstance() {
        return newInstance(false);
    }

    public abstract ProxyProvider newInstance(boolean useJavaProxiesIfPossible);

}
