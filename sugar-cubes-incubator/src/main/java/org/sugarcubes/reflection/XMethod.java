package org.sugarcubes.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<T> extends XReflectionObjectImpl<Method>
    implements XExecutable<T>, XMember<Method>, XModifiers {

    XMethod(Method reflectionObject) {
        super(reflectionObject);
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
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

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public T invoke(Object obj, Object... args) {
        return XReflectionUtils.execute(() -> getReflectionObject().invoke(obj, args));
    }

    private T invokeDefault(Object proxy, Method method, Object[] args) {
        MethodHandles.Lookup lookup = XReflection.of(MethodHandles.Lookup.class)
            .<MethodHandles.Lookup>getField("IMPL_LOOKUP")
            .get(null);
        return XReflectionUtils.execute(() -> lookup
            .in(method.getDeclaringClass())
            .unreflectSpecial(method, method.getDeclaringClass())
            .bindTo(proxy)
            .invokeWithArguments(args));
    }

}
