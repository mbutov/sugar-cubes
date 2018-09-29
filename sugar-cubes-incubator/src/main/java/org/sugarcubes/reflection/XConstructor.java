package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XReflectionObject<Constructor<T>> implements XExecutable<T> {

    public XConstructor(Constructor<T> reflectionObject) {
        super(reflectionObject);
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
        return XReflection.execute(() -> getReflectionObject().newInstance(args));
    }

}
