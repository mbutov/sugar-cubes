package org.sugarcubes.reflection;

/**
 * @author Maxim Butov
 */
public interface XFieldAccessor<T> {

    XField<T> getField();

    T get();

    void set(T value);

    T put(T value);

}
