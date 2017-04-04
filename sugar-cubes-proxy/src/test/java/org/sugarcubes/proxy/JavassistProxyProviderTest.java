package org.sugarcubes.proxy;

/**
 * @author Maxim Butov
 */
public class JavassistProxyProviderTest extends AbstractProxyProviderTests {

    @Override
    protected DefaultProxyProvider getProxyProvider() {
        return new JavassistProxyProvider();
    }

}
