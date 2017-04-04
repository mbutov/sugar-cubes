package org.sugarcubes.valueholder.context;

import java.util.HashMap;
import java.util.Map;

//CHECKSTYLE:OFF

/**
 * @author Maxim Butov
 */
public abstract class AbstractStoreValueContext<S> implements ValueContext {

    private S storeContext;

    public S getStoreContext() {
        return storeContext;
    }

    public void setStoreContext(S storeContext) {
        this.storeContext = storeContext;
    }

    @Override
    public Map getStore() {
        Object store = getFromContext();
        if (store == null) {
            store = new HashMap();
            putToContext(store);
        }
        return (Map) store;
    }

    @Override
    public void remove() {
        removeFromContext();
    }

    protected abstract Object getFromContext();

    protected abstract void putToContext(Object store);

    protected abstract void removeFromContext();

}

//CHECKSTYLE:ON
