package org.sugarcubes.reflection;

import java.io.Serializable;

/**
 * Abstract reflection wrapper.
 *
 * @author Maxim Butov
 */
public abstract class XReflectionObjectImpl<T> implements XReflectionObject<T>, Serializable {

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
