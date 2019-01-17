package org.sugarcubes.reflection;

import java.lang.reflect.Method;

import static org.sugarcubes.reflection.XReflectionUtils.execute;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XReloadableReflectionObject<Method>
    implements XAnnotated<Method>, XExecutable<R>, XMember<Method>, XModifiers, XTyped<R> {

    private final Class<?> declaringClass;
    private final String name;
    private final Class<?>[] parameterTypes;

    XMethod(Method reflectionObject) {
        super(reflectionObject);
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.parameterTypes = reflectionObject.getParameterTypes();
    }

    @Override
    protected Method loadReflectionObject() throws ReflectiveOperationException {
        return declaringClass.getDeclaredMethod(name, parameterTypes);
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes.clone();
    }

    @Override
    public Class<R> getType() {
        return (Class) getReflectionObject().getReturnType();
    }

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return execute(() -> getReflectionObject().invoke(obj, args));
    }

    public <X> XMethod<X> cast() {
        return (XMethod) this;
    }

}
