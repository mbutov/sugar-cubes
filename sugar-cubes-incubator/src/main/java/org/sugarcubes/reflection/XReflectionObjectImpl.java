package org.sugarcubes.reflection;

import java.io.Serializable;
import java.util.Objects;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public abstract class XReflectionObjectImpl<T> implements XReflectionObject<T>, Serializable {

    private transient T reflectionObject;

    XReflectionObjectImpl(T reflectionObject) {
        this.reflectionObject = Objects.requireNonNull(reflectionObject);
    }

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = reloadReflectionObject();
        }
        return reflectionObject;
    }

    protected T reloadReflectionObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XReflectionObject)) {
            return false;
        }
        XReflectionObject that = (XReflectionObject) obj;
        return getReflectionObject().equals(that.getReflectionObject());
    }

    @Override
    public int hashCode() {
        return getReflectionObject().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + getReflectionObject() + '}';
    }
    
}
