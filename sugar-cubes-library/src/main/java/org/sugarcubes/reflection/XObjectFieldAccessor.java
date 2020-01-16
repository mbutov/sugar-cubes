package org.sugarcubes.reflection;

/**
 * @author Maxim Butov
 */
public interface XObjectFieldAccessor<T> {

    Object getObject();

    XField<T> getField();

    default T get() {
        return getField().get(getObject());
    }

    default void set(T value) {
        getField().set(getObject(), value);
    }

    default T put(T value) {
        return getField().getAndSet(getObject(), value);
    }

}
