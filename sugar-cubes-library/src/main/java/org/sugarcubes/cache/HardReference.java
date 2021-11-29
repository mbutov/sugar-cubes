package org.sugarcubes.cache;

import java.lang.ref.WeakReference;

/**
 * Hard object reference.
 *
 * @author Maxim Butov
 */
public class HardReference<T> extends WeakReference<T> {

    private final T referent;

    public HardReference(T referent) {
        super(null);
        this.referent = referent;
    }

    @Override
    public T get() {
        return referent;
    }

}
