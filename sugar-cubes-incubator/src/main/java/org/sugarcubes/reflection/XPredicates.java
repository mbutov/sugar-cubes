package org.sugarcubes.reflection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public interface XPredicates {

    static Predicate<XReflectionObjectImpl> withName(String name) {
        Objects.requireNonNull(name);
        return xReflectionObject -> xReflectionObject.getName().equals(name);
    }

    static Predicate<XExecutable<?>> withParameterTypes(Class... types) {
        Objects.requireNonNull(types);
        return xExecutable -> xExecutable.hasParameterTypes(types);
    }

}
