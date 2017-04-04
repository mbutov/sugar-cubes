package org.sugarcubes.proxy;

import java.io.File;
import java.io.IOException;

/**
 * @author Maxim Butov
 */
public class DexmakerProxyProvider extends InstantiatorProxyProvider {

    public DexmakerProxyProvider() {
        this(false);
    }

    public DexmakerProxyProvider(boolean useJavaProxiesIfPossible) {
        super(useJavaProxiesIfPossible);
    }

    private File dexCache;

    public File getDexCache() {
        if (dexCache == null) {
            try {
                dexCache = File.createTempFile("dexmaker", ".dexcache");
                dexCache.delete();
                dexCache.mkdirs();
            }
            catch (IOException e) {
                throw new ProxyError("Can't create dex cache", e);
            }
        }
        return dexCache;
    }

    public void setDexCache(File dexCache) {
        this.dexCache = dexCache;
    }

    @Override
    protected <T> ProxyFactory<T> newCustomProxyFactory(ClassLoader classLoader, GenericType type) {
        return new DexmakerProxyFactory<T>(classLoader, getDexCache(), type, getInstantiatorStrategy());
    }

}
