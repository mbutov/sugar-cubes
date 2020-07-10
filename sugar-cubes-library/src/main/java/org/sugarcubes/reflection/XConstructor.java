package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;

import static org.sugarcubes.reflection.XReflectionUtils.execute;

/**
 * Wrapper for {@link Constructor}.
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XExecutable<T, Constructor<T>> {

    XConstructor(Constructor<T> reflectionObject) {
        super(reflectionObject, reflectionObject.getParameterTypes());
    }

    @Override
    protected Constructor<T> loadReflectionObject() throws ReflectiveOperationException {
        return declaringClass.getDeclaredConstructor(parameterTypes);
    }

    public T newInstance(Object... args) {
        return execute(() -> getReflectionObject().newInstance(args));
    }

}
