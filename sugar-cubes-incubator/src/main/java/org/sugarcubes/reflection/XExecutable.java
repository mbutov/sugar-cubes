package org.sugarcubes.reflection;

import java.util.Arrays;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public interface XExecutable<T> {

    Class[] getParameterTypes();

    default boolean argumentsMatch(Object... args) {
        Class[] parameterTypes = getParameterTypes();
        if (args.length != parameterTypes.length) {
            return false;
        }
        for (int k = 0; k < args.length; k++) {
            if (args == null) {
                if (!parameterTypes[k].isPrimitive()) {
                    return false;
                }
            }
            else {
                if (!parameterTypes[k].isInstance(args[k])) {
                    return false;
                }
            }
        }
        return true;
    }

    default boolean hasParameterTypes(Class... types) {
        return Arrays.equals(types, getParameterTypes());
    }

}
