package org.sugarcubes.reflection;

import java.lang.reflect.Method;

import static org.sugarcubes.reflection.XReflectionUtils.execute;
import static org.sugarcubes.reflection.XReflectionUtils.tryToMakeAccessible;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XReloadableReflectionObject<Method>
    implements XAnnotated<Method>, XExecutable<R>, XMember<Method>, XModifiers {

    private final Class declaringClass;
    private final String name;
    private final Class[] parameterTypes;

    XMethod(Method reflectionObject) {
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.parameterTypes = reflectionObject.getParameterTypes();
    }

    @Override
    protected Method loadReflectionObject() {
        return execute(() -> tryToMakeAccessible(declaringClass.getDeclaredMethod(name, parameterTypes)));
    }

    @Override
    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return execute(() -> getReflectionObject().invoke(obj, args));
    }

}
