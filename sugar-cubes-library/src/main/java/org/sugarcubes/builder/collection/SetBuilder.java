package org.sugarcubes.builder.collection;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * Set builders.
 *
 * @author Maxim Butov
 */
public class SetBuilder<X, S extends Set<X>> extends CollectionBuilder<X, S, SetBuilder<X, S>> {

    public SetBuilder(Supplier<S> supplier) {
        super(supplier);
    }

    public Set<X> unmodifiable() {
        return transform(Collections::unmodifiableSet).build();
    }

    public static <X, S extends Set<X>> SetBuilder<X, S> set(Supplier<S> supplier) {
        return new SetBuilder<>(supplier);
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

    public static <X extends Enum<X>> SetBuilder<X, EnumSet<X>> enumSet(Class<X> elementType) {
        return set(() -> EnumSet.noneOf(elementType));
    }

    public static <X> SetBuilder<X, Set<X>> fromMap(Supplier<Map<X, Boolean>> supplier) {
        return set(() -> Collections.newSetFromMap(supplier.get()));
    }

    public static <X> SetBuilder<X, Set<X>> identitySet() {
        return fromMap(IdentityHashMap<X, Boolean>::new);
    }

    public static <X> SetBuilder<X, Set<X>> weakSet() {
        return fromMap(WeakHashMap<X, Boolean>::new);
    }

    public static <X> Set<X> unmodifiableHashSet(X... elements) {
        return SetBuilder.<X>hashSet().addAll(elements).unmodifiable();
    }

    public static <X> Set<X> unmodifiableLinkedHashSet(X... elements) {
        return SetBuilder.<X>linkedHashSet().addAll(elements).unmodifiable();
    }

    public static <X extends Enum<X>> Set<X> unmodifiableEnumSet(X first, X... rest) {
        return set(() -> EnumSet.of(first, rest)).unmodifiable();
    }

}
