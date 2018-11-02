package org.sugarcubes.reflection;

import java.lang.reflect.Method;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XReflectionObjectImpl<Method>
    implements XAnnotated<Method>, XExecutable<R>, XMember<Method>, XModifiers {

    private transient Method reflectionObject;

    private final Class declaringClass;
    private final String name;
    private final Class[] parameterTypes;

    XMethod(Method reflectionObject) {

        this.reflectionObject = reflectionObject;

        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.parameterTypes = reflectionObject.getParameterTypes();

        XReflectionUtils.tryToMakeAccessible(this.reflectionObject);

    }

    @Override
    public Method getReflectionObject() {
        if (reflectionObject == null) {
            reflectionObject = XReflectionUtils.execute(() -> declaringClass.getDeclaredMethod(name, parameterTypes));
            XReflectionUtils.tryToMakeAccessible(reflectionObject);
        }
        return reflectionObject;
    }

    @Override
    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return XReflectionUtils.execute(() -> getReflectionObject().invoke(obj, args));
    }

}
