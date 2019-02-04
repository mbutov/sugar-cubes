package org.sugarcubes.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.sugarcubes.stream.ZeroOneCollectors;
import static org.sugarcubes.reflection.XPredicates.withName;
import static org.sugarcubes.reflection.XPredicates.withReflectionObject;
import static org.sugarcubes.reflection.XReflectionUtils.execute;

/**
 * Wrapper for {@link Method}.
 *
 * @author Maxim Butov
 */
public class XMethod<R> extends XReloadableReflectionObject<Method>
    implements XAnnotated<Method>, XExecutable<R>, XMember<Method>, XModifiers {

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

    public boolean hasNameAndParameterTypes(String name, Class... types) {
        return hasName(name) && hasParameterTypes(types);
    }

    public R invoke(Object obj, Object... args) {
        return execute(() -> getReflectionObject().invoke(obj, args));
    }

    public <X> XMethod<X> cast() {
        return (XMethod) this;
    }

    public <X> XMethod<X> getSuper() {
        Method method = getReflectionObject();
        if (method.isBridge()) {
            // todo: this is not working
            return getDeclaringClass().getSuperclass().getMethods()
                .filter(withName(name))
                .filter(withReflectionObject(candidate -> candidate.getReturnType().isAssignableFrom(method.getReturnType()) &&
                    Arrays.equals(method.getGenericParameterTypes(), candidate.getGenericParameterTypes())))
                .collect(ZeroOneCollectors.onlyElement())
                .cast();
        }
        else {
            return getDeclaringClass().getSuperclass().<R>findMethod(name, parameterTypes).orElse(this).cast();
        }
    }

    public <X> XMethod<X> getRoot() {
        XMethod<X> baseMethod = getSuper();
        return this == baseMethod ? cast() : baseMethod.getRoot();
    }

}
