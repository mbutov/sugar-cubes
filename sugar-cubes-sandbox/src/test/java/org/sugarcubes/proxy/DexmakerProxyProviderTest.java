package org.sugarcubes.proxy;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class DexmakerProxyProviderTest extends AbstractProxyProviderTests {

    private DexmakerProxyProvider proxyProvider;

    @BeforeEach
    public void before() {
        proxyProvider = new DexmakerProxyProvider();
    }

    @AfterEach
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
    @Test
    public void testClassProxy() throws Exception {
        Assertions.assertThrows(Throwable.class, () ->
            super.testClassProxy());
    }

    @Override
    @Test
    public void testFinalClass() {
        Assertions.assertThrows(Throwable.class, () ->
            super.testFinalClass());
    }

    @Override
    @Test
    public void testSerializable() {
        Assertions.assertThrows(Throwable.class, () ->
            super.testSerializable());
    }
}
