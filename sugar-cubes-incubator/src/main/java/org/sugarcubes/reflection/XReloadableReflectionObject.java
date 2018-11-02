package org.sugarcubes.reflection;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public abstract class XReloadableReflectionObject<T> extends XReflectionObjectImpl<T> {

    private transient T reflectionObject;

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = loadReflectionObject();
        }
        return reflectionObject;
    }

    protected abstract T loadReflectionObject();

}
