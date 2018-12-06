package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;

import static org.sugarcubes.reflection.XReflectionUtils.execute;
import static org.sugarcubes.reflection.XReflectionUtils.tryToMakeAccessible;

/**
 * Wrapper for {@link Constructor}.
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XReloadableReflectionObject<Constructor<T>>
    implements XAnnotated<Constructor<T>>, XExecutable<T>, XMember<Constructor<T>>, XModifiers {

    private final Class<T> declaringClass;
    private final Class[] parameterTypes;

    XConstructor(Constructor<T> reflectionObject) {
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.parameterTypes = reflectionObject.getParameterTypes();
    }

    @Override
    protected Constructor<T> loadReflectionObject() {
        return tryToMakeAccessible(execute(() -> declaringClass.getDeclaredConstructor(parameterTypes)));
    }

    @Override
    public Class[] getParameterTypes() {
        return getReflectionObject().getParameterTypes();
    }

    public T newInstance(Object... args) {
        return execute(() -> getReflectionObject().newInstance(args));
    }

}
