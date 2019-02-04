package org.sugarcubes.reflection;

/**
 * @author Maxim Butov
 */
public interface XFieldAccessor<T> {

    T get();

    void set(T value);

    T put(T value);

}
