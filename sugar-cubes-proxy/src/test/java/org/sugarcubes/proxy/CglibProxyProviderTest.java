package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class CglibProxyProviderTest extends AbstractProxyProviderTests {

    @Override
    protected DefaultProxyProvider getProxyProvider() {
        return new CglibProxyProvider();
    }

}
