package org.sugarcubes.reflection;

import java.lang.reflect.Executable;
import java.util.function.Predicate;

import org.sugarcubes.check.Checks;
import static org.sugarcubes.function.Predicates.and;

/**
 * Predicates which simplify filtering of class members.
 *
 * @author Maxim Butov
 */
public class XPredicates {

    public static <X, R extends XReflectionObject<X>> Predicate<R> withName(String name) {
        Checks.arg().notNull(name, "name must not be null");
        return obj -> obj.hasName(name);
    }

    public static <T, E extends Executable, R extends XExecutable<T, E>> Predicate<R> withParameterTypes(Class<?>... types) {
        Checks.arg().notNull(types, "types must not be null");
        return obj -> obj.hasParameterTypes(types);
    }

    public static <T, R extends XMethod<T>> Predicate<R> withNameAndParameterTypes(String name, Class<?>... types) {
        return and(XPredicates.withName(name), XPredicates.withParameterTypes(types));
    }

    public static <X, R extends XReflectionObject<X>> Predicate<R> withReflectionObject(Predicate<X> predicate) {
        return obj -> predicate.test(obj.getReflectionObject());
    }

}
