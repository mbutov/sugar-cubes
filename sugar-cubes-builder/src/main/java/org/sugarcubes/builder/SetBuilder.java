package org.sugarcubes.builder;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 *
 * @author Maxim Butov
 */
public class SetBuilder<X, S extends Set<X>> extends CollectionBuilder<X, S> {

    public SetBuilder(S value) {
        super(value);
    }

    public SetBuilder(Supplier<S> supplier) {
        super(supplier);
    }

    public static <X, S extends Set<X>> SetBuilder<X, S> set(Supplier<S> supplier) {
        return new SetBuilder<X, S>(supplier);
    }

    public static <X> SetBuilder<X, Set<X>> hashSet() {
        return set(HashSet::new);
    }

    public static <X> SetBuilder<X, Set<X>> linkedHashSet() {
        return set(LinkedHashSet::new);
    }

    public static <X> SetBuilder<X, NavigableSet<X>> treeSet() {
        return set(TreeSet::new);
    }

}
