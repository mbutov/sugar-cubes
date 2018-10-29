package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XReflectionObject<Constructor<T>> implements XExecutable<T> {

    XConstructor(Constructor<T> reflectionObject) {
        super(reflectionObject);
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    @Override
    public Class[] getParameterTypes() {
        return getReflectionObject().getParameterTypes();
    }

    public T newInstance(Object... args) {
        return XReflectionUtils.execute(() -> getReflectionObject().newInstance(args));
    }

}
