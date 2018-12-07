package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public abstract class XReloadableReflectionObject<T extends AccessibleObject> extends XReflectionObjectImpl<T> {

    private transient T reflectionObject;

    protected XReloadableReflectionObject(T reflectionObject) {
        this.reflectionObject = reflectionObject;
        prepare(reflectionObject);
    }

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = XReflectionUtils.execute(this::loadReflectionObject);
            prepare(reflectionObject);
        }
        return reflectionObject;
    }

    protected abstract T loadReflectionObject() throws ReflectiveOperationException;

    protected void prepare(T reflectionObject) {
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

}
