package org.sugarcubes.singleton;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.sugarcubes.rex.Rex;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class SingletonHolder<T> implements Supplier<T> {

    private final Callable<T> loader;

    public SingletonHolder(Callable<T> loader) {
        this.loader = loader;
    }

    public SingletonHolder(Supplier<T> loader) {
        this((Callable<T>) loader::get);
    }

    private T value;

    private synchronized T loadValue() {
        if (value == null) {
            try {
                value = loader.call();
            }
            catch (Exception e) {
                throw Rex.rethrowAsRuntime(e);
            }
        }
        return value;
    }

    @Override
    public T get() {
        return value != null ? value : loadValue();
    }

}
