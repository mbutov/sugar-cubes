package org.sugarcubes.reflection;

import java.lang.reflect.Method;

import static org.sugarcubes.reflection.XReflectionUtils.execute;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XExecutable<R, Method> {

    private final String name;

    XMethod(Method reflectionObject) {
        super(reflectionObject, reflectionObject.getParameterTypes());
        this.name = reflectionObject.getName();
    }

    @Override
    protected Method loadReflectionObject() throws ReflectiveOperationException {
        return declaringClass.getDeclaredMethod(name, parameterTypes);
    }

    public boolean hasNameAndParameterTypes(String name, Class<?>... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return execute(() -> getReflectionObject().invoke(obj, args));
    }

    public <X> XMethod<X> cast() {
        return (XMethod) this;
    }

    public <X> XMethod<X> getSuper() {
        // todo: check this
        return getDeclaringClass().getSuperclass().<R>findMethod(name, parameterTypes).orElse(this).cast();
    }

    public <X> XMethod<X> getRoot() {
        XMethod<X> baseMethod = getSuper();
        return this == baseMethod ? cast() : baseMethod.getRoot();
    }

}
