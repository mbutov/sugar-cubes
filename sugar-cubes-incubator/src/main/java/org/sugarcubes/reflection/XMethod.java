package org.sugarcubes.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XMethod<T> extends XReflectionObject<Method> implements XModifiers, XExecutable<T> {

    public XMethod(Method reflectionObject) {
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
