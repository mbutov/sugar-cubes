package org.sugarcubes.singleton;

import java.util.function.Supplier;

import org.sugarcubes.rex.Rex;
import org.sugarcubes.unsafe.UnsafeCallable;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class SingletonHolder<T> implements Supplier<T> {

    private final UnsafeCallable<T> loader;
    private final boolean safe;

    public SingletonHolder(UnsafeCallable<T> loader, boolean safe) {
        this.loader = loader;
        this.safe = safe;
    }

    public SingletonHolder(UnsafeCallable<T> loader) {
        this(loader, false);
    }

    private volatile boolean loaded;
    private T value;

    private synchronized T loadValue() {
        if (!loaded) {
            try {
                value = loader.call();
            }
            catch (Throwable e) {
                if (!safe) {
                    throw Rex.rethrowAsRuntime(e);
                }
            }
            finally {
                loaded = true;
            }
        }
        return value;
    }

    @Override
    public T get() {
        return loaded ? value : loadValue();
    }

}
