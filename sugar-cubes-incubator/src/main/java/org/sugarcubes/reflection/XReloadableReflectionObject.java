package org.sugarcubes.reflection;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public abstract class XReloadableReflectionObject<T> extends XReflectionObjectImpl<T> {

    private transient T reflectionObject;

    protected XReloadableReflectionObject() {
    }

    protected XReloadableReflectionObject(T reflectionObject) {
        this.reflectionObject = reflectionObject;
    }

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = loadReflectionObject();
        }
        return reflectionObject;
    }

    protected abstract T loadReflectionObject();

}
