package org.sugarcubes.proxy;

import org.objenesis.strategy.InstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author Maxim Butov
 */
public abstract class InstantiatorProxyProvider extends DefaultProxyProvider {

    protected InstantiatorProxyProvider() {
    }

    protected InstantiatorProxyProvider(boolean useJavaProxiesIfPossible) {
        super(useJavaProxiesIfPossible);
    }

    private InstantiatorStrategy instantiatorStrategy;

    public InstantiatorStrategy getInstantiatorStrategy() {
        if (instantiatorStrategy == null) {
            instantiatorStrategy = new StdInstantiatorStrategy();
        }
        return instantiatorStrategy;
    }

    public void setInstantiatorStrategy(InstantiatorStrategy instantiatorStrategy) {
        this.instantiatorStrategy = instantiatorStrategy;
    }

}
