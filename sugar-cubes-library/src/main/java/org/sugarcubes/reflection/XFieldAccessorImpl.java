package org.sugarcubes.reflection;

import java.util.Objects;

/**
 * @author Maxim Butov
 */
class XFieldAccessorImpl<T> implements XFieldAccessor<T> {

    private final XField<T> xField;
    private final Object obj;

    XFieldAccessorImpl(XField<T> xField, Object obj) {
        this.xField = xField;
        this.obj = obj;
    }

    @Override
    public T get() {
        return xField.get(obj);
    }

    @Override
    public void set(T value) {
        xField.set(obj, value);
    }

    @Override
    public T put(T value) {
        return xField.put(obj, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XFieldAccessorImpl)) {
            return false;
        }
        XFieldAccessorImpl that = (XFieldAccessorImpl) obj;
        return Objects.equals(xField, that.xField) && Objects.equals(this.obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xField, obj);
    }

}
