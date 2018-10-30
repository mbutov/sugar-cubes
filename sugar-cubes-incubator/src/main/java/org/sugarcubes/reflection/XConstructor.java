package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;

/**
 * Wrapper for {@link Constructor}.
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XReflectionObjectImpl<Constructor<T>>
    implements XAnnotated<Constructor<T>>, XExecutable<T>, XMember<Constructor<T>>, XModifiers {

    XConstructor(Constructor<T> reflectionObject) {
        super(reflectionObject);
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    @Override
    public Class[] getParameterTypes() {
        return getReflectionObject().getParameterTypes();
    }

    public T newInstance(Object... args) {
        return XReflectionUtils.execute(() -> getReflectionObject().newInstance(args));
    }

}
