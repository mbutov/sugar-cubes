package org.sugarcubes.cloner;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.sugarcubes.arg.Arg;
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

    static <X> Set<X> asSet(X first, X... others) {
        Arg.notNull(first, "first argument must be not null");
        return SetBuilder.<X>hashSet().add(first).addAll(others).build();
    }

    static Predicate<Class<?>> isAnyOf(Collection<Class<?>> classes) {
        return classes::contains;
    }

    static Predicate<Class<?>> isSubclassOf(Collection<Class<?>> classes) {
        return clazz -> anyMatch(classes.stream().map(cl -> (Predicate<Class<?>>) cl::isAssignableFrom), clazz);
    }

}
