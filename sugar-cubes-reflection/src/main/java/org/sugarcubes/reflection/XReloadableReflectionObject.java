package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * A superclass for reflection wrappers which have transient part and must be reloaded after deserialization.
 *
 * @author Maxim Butov
 */
public abstract class XReloadableReflectionObject<T extends AccessibleObject> extends XReflectionObjectImpl<T> {

    private transient T reflectionObject;

    protected XReloadableReflectionObject(T reflectionObject) {
        this.reflectionObject = reflectionObject;
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = XReflectionUtils.execute(this::loadReflectionObject);
            XReflectionUtils.tryToMakeAccessible(reflectionObject);
        }
        return reflectionObject;
    }

    protected abstract T loadReflectionObject() throws ReflectiveOperationException;

}
