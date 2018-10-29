package org.sugarcubes.reflection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public interface XPredicates {

    static Predicate<XReflectionObject> withName(String name) {
        Objects.requireNonNull(name);
        return xReflectionObject -> xReflectionObject.getName().equals(name);
    }

    static Predicate<XExecutable<?>> withParameterTypes(Class... types) {
        Objects.requireNonNull(types);
        return xExecutable -> xExecutable.hasParameterTypes(types);
    }

}
