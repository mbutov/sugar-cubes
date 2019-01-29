package org.sugarcubes.reflection;

import java.util.Arrays;

/**
 * Common part of constructor and method.
 *
 * @author Maxim Butov
 */
public interface XExecutable<T> {

    Class<?>[] getParameterTypes();

    default boolean hasParameterTypes(Class... types) {
        return Arrays.equals(types, getParameterTypes());
    }

    default boolean argumentsMatch(Object... args) {
        Class[] parameterTypes = getParameterTypes();
        if (args.length != parameterTypes.length) {
            return false;
        }
        for (int k = 0; k < args.length; k++) {
            Object arg = args[k];
            Class parameterType = parameterTypes[k];
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
