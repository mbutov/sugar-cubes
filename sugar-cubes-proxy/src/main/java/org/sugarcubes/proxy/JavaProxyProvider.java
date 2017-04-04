package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class JavaProxyProvider extends DefaultProxyProvider {

    @Override
    public boolean canProxy(Class clazz) {
        return GenericType.create(clazz).isInterfacesOnly();
    }

}
