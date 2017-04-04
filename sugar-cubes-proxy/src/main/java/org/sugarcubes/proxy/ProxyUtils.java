package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class ProxyUtils {

    public static boolean maybeProxy(Class clazz) {
        return java.lang.reflect.Proxy.class.isAssignableFrom(clazz) || clazz.getName().contains("$$");
    }

    public static boolean isProxyClass(Class clazz) {
        if (!maybeProxy(clazz)) {
            return false;
        }
        for (ProxyProviderFactory factory : ProxyProviderFactory.FACTORIES) {
            if (factory.isAvailable(clazz.getClassLoader()) && factory.isProxyClass(clazz)) {
                return true;
            }
        }
        return false;
    }

}
