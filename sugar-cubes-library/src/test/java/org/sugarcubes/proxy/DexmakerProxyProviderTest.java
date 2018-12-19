package org.sugarcubes.proxy;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class DexmakerProxyProviderTest extends AbstractProxyProviderTests {

    private DexmakerProxyProvider proxyProvider;

    @Before
    public void before() {
        proxyProvider = new DexmakerProxyProvider();
    }

    @After
    public void after() {
        for (File file : proxyProvider.getDexCache().listFiles()) {
            file.delete();
        }
        proxyProvider.getDexCache().delete();
    }

    @Override
    protected DefaultProxyProvider getProxyProvider() {
        return proxyProvider;
    }

    @Override
    @Test(expected = Throwable.class)
    public void testClassProxy() throws Exception {
        super.testClassProxy();
    }

    @Override
    @Test(expected = Throwable.class)
    public void testFinalClass() {
        super.testFinalClass();
    }

    @Override
    @Test(expected = Throwable.class)
    public void testSerializable() {
        super.testSerializable();
    }
}
