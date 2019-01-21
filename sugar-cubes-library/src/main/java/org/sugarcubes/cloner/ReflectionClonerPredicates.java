package org.sugarcubes.cloner;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.sugarcubes.builder.collection.SetBuilder;

/**
 * @author Maxim Butov
 */
class ReflectionClonerPredicates {

    static <X> boolean anyMatch(Collection<Predicate<X>> predicates, X x) {
        return anyMatch(predicates.stream(), x);
    }

    static <X> boolean anyMatch(Stream<Predicate<X>> predicates, X x) {
        return predicates.anyMatch(p -> p.test(x));
    }

    static Predicate<Class<?>> isAnyOf(Class<?>... classes) {
        return isAnyOf(SetBuilder.<Class<?>>hashSet().addAll(classes).build());
    }

    static Predicate<Class<?>> isAnyOf(Collection<Class<?>> classes) {
        return classes::contains;
    }

}
