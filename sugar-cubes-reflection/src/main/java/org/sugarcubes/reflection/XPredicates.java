package org.sugarcubes.reflection;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public class XPredicates {

    public static <X, R extends XReflectionObject<X>> Predicate<R> withName(String name) {
        Objects.requireNonNull(name);
        return obj -> obj.hasName(name);
    }

    public static <T, R extends XExecutable<T>> Predicate<R> withParameterTypes(Class... types) {
        Objects.requireNonNull(types);
        return obj -> obj.hasParameterTypes(types);
    }

    public static <T, R extends XMethod<T>> Predicate<R> withNameAndParameterTypes(String name, Class... types) {
        Predicate<R> predicate1 = withName(name);
        Predicate<R> predicate2 = withParameterTypes(types);
        return predicate1.and(predicate2);
    }

}
