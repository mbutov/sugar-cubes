package org.sugarcubes.reflection;

import java.util.Objects;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public abstract class XReflectionObject<T> {

    private final T reflectionObject;

    protected XReflectionObject(T reflectionObject) {
        this.reflectionObject = Objects.requireNonNull(reflectionObject);
    }

    public T getReflectionObject() {
        return reflectionObject;
    }

    public abstract String getName();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XReflectionObject)) {
            return false;
        }
        XReflectionObject that = (XReflectionObject) obj;
        return reflectionObject.equals(that.reflectionObject);
    }

    @Override
    public int hashCode() {
        return reflectionObject.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + reflectionObject + '}';
    }
    
}
