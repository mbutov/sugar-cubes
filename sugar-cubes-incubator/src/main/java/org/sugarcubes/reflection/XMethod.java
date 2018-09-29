package org.sugarcubes.reflection;

import java.lang.reflect.Method;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XMethod<T> extends XReflectionObject<Method> implements XModifiers, XExecutable<T> {

    public XMethod(Method javaObject) {
        super(javaObject);
    }

    @Override
    public int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    @Override
    public Class[] getParameterTypes() {
        return getReflectionObject().getParameterTypes();
    }

    public T invoke(Object obj, Object... args) {
        return XReflection.execute(() -> getReflectionObject().invoke(obj, args));
    }

}
