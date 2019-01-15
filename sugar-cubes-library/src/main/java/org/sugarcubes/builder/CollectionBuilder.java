package org.sugarcubes.builder;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Base class for List and Set builders.
 *
 * @author Maxim Butov
 */
public class CollectionBuilder<X, C extends Collection<X>> extends MutableBuilder<C, CollectionBuilder<X, C>> {

    public CollectionBuilder(Supplier<C> supplier) {
        super(supplier);
    }

    public static <X, C extends Collection<X>> CollectionBuilder<X, C> collection(Supplier<C> supplier) {
        return new CollectionBuilder<X, C>(supplier);
    }

    public <D extends Collection<X>> CollectionBuilder<X, D> cast() {
        return (CollectionBuilder<X, D>) this;
    }

    public CollectionBuilder<X, C> add(X element) {
        return apply(collection -> collection.add(element));
    }

    public CollectionBuilder<X, C> add(X... elements) {
        return addAll(elements);
    }

    public CollectionBuilder<X, C> addAll(Collection<X> elements) {
        return apply(collection -> collection.addAll(elements));
    }

    public CollectionBuilder<X, C> addAll(X... elements) {
        return apply(collection -> collection.addAll(Arrays.asList(elements)));
    }

    public X[] toArray(Class<? super X> componentType) {
        return toArray(size -> (X[]) Array.newInstance(componentType, size));
    }

    public X[] toArray(IntFunction<X[]> generator) {
        return build().stream().toArray(generator);
    }

}
