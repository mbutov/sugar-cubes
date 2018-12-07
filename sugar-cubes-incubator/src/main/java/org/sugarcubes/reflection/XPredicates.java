package org.sugarcubes.reflection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public class XPredicates {

    public static <X> Predicate<XReflectionObject<X>> withName(String name) {
        Objects.requireNonNull(name);
        return obj -> obj.hasName(name);
    }

    public static <T> Predicate<XExecutable<T>> withParameterTypes(Class... types) {
        Objects.requireNonNull(types);
        return obj -> obj.hasParameterTypes(types);
    }

}
