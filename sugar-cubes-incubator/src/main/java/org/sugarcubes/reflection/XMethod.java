package org.sugarcubes.reflection;

import java.lang.reflect.Method;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XReflectionObjectImpl<Method>
    implements XAnnotated<Method>, XExecutable<R>, XMember<Method>, XModifiers {

    XMethod(Method reflectionObject) {
        super(reflectionObject);
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    @Override
    public Class[] getParameterTypes() {
        return getReflectionObject().getParameterTypes();
    }

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return XReflectionUtils.execute(() -> getReflectionObject().invoke(obj, args));
    }

}
