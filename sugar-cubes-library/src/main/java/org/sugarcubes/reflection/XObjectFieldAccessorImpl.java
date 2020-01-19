package org.sugarcubes.reflection;

import java.util.Objects;

import org.sugarcubes.check.Checks;

/**
 * @author Maxim Butov
 */
class XObjectFieldAccessorImpl<T> implements XObjectFieldAccessor<T> {

    private final XField<T> xField;
    private final Object obj;

    XObjectFieldAccessorImpl(Object obj, XField<T> xField) {
        this.obj = Checks.arg().notNull(obj, "obj must not be null");
        this.xField = Checks.arg().notNull(xField, "field must not be null");
    }

    @Override
    public XField<T> getField() {
        return xField;
    }

    @Override
    public Object getObject() {
        return obj;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XObjectFieldAccessorImpl)) {
            return false;
        }
        XObjectFieldAccessorImpl that = (XObjectFieldAccessorImpl) obj;
        return Objects.equals(xField, that.xField) && Objects.equals(this.obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xField, obj);
    }

}
