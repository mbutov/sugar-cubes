package org.sugarcubes.reflection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public class XPredicates {

    public static Predicate<XReflectionObject> withName(String name) {
        Objects.requireNonNull(name);
        return obj -> obj.hasName(name);
    }

    public static Predicate<XExecutable<?>> withParameterTypes(Class... types) {
        Objects.requireNonNull(types);
        return obj -> obj.hasParameterTypes(types);
    }

}
