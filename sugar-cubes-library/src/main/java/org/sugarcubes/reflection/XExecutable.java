package org.sugarcubes.reflection;

import java.lang.reflect.Executable;
import java.util.Arrays;

/**
 * Common part of {@link XConstructor} and {@link XMethod}.
 *
 * @author Maxim Butov
 */
public abstract class XExecutable<T, E extends Executable> extends XReloadableReflectionObject<E>
    implements XAnnotated<E>, XMember<E>, XModifiers {

    protected final Class<T> declaringClass;
    protected final Class<?>[] parameterTypes;

    protected XExecutable(E reflectionObject, Class<?>[] parameterTypes) {
        super(reflectionObject);
        this.declaringClass = (Class<T>) reflectionObject.getDeclaringClass();
        this.parameterTypes = parameterTypes;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes.clone();
    }

    public boolean hasParameterTypes(Class<?>... types) {
        return Arrays.equals(types, parameterTypes);
    }

    public boolean argumentsMatch(Object... args) {
        if (args.length != parameterTypes.length) {
            return false;
        }
        for (int k = 0; k < args.length; k++) {
            Object arg = args[k];
            Class<?> parameterType = parameterTypes[k];
            if (arg == null) {
                if (parameterType.isPrimitive()) {
                    return false;
                }
            }
            else {
                if (!parameterType.isInstance(arg)) {
                    return false;
                }
            }
        }
        return true;
    }

}
