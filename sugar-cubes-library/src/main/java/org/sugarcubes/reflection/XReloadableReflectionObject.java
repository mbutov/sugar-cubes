package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * A superclass for reflection wrappers which have transient part and must be reloaded after deserialization.
 *
 * @author Maxim Butov
 */
public abstract class XReloadableReflectionObject<T extends AccessibleObject> extends XReflectionObjectImpl<T> {

    /**
     * Constructor or field or method.
     */
    private transient T reflectionObject;

    protected XReloadableReflectionObject(T reflectionObject) {
        this.reflectionObject = XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    @Override
    public T getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = XReflectionUtils.tryToMakeAccessible(XReflectionUtils.execute(this::loadReflectionObject));
        }
        return reflectionObject;
    }

    /**
     * Loads reflection object by its properties after deserialization.
     *
     * @return initial reflection object
     */
    protected abstract T loadReflectionObject() throws ReflectiveOperationException;

}
