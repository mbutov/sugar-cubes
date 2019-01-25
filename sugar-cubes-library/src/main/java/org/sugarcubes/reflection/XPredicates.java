package org.sugarcubes.reflection;

import java.util.function.Predicate;

import org.sugarcubes.arg.Arg;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public class XPredicates {

    public static <X, R extends XReflectionObject<X>> Predicate<R> withName(String name) {
        Arg.notNull(name, "name must not be null");
        return obj -> obj.hasName(name);
    }

    public static <T, R extends XExecutable<T>> Predicate<R> withParameterTypes(Class... types) {
        Arg.notNull(types, "types must not be null");
        return obj -> obj.hasParameterTypes(types);
    }

    public static <T, R extends XMethod<T>> Predicate<R> withNameAndParameterTypes(String name, Class... types) {
        Predicate<R> predicate1 = withName(name);
        Predicate<R> predicate2 = withParameterTypes(types);
        return predicate1.and(predicate2);
    }

    public static <X, R extends XReflectionObject<X>> Predicate<R> withReflectionObject(Predicate<X> predicate) {
        return obj -> predicate.test(obj.getReflectionObject());
    }

}
