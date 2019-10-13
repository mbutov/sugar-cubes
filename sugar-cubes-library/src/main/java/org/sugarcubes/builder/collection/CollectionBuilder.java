package org.sugarcubes.builder.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.sugarcubes.builder.MutableBuilder;

/**
 * Base class for List and Set builders.
 *
 * @author Maxim Butov
 */
public abstract class CollectionBuilder<X, C extends Collection<X>, B extends CollectionBuilder<X, C, B>> extends MutableBuilder<C, B> {

    protected CollectionBuilder(Supplier<C> supplier) {
        super(supplier);
    }

    public B add(X element) {
        return apply(collection -> collection.add(element));
    }

    public B add(Optional<X> element) {
        return apply(list -> element.ifPresent(list::add));
    }

    public B addAll(Collection<X> elements) {
        return apply(collection -> collection.addAll(elements));
    }

    public B addAll(X... elements) {
        return addAll(Stream.of(elements));
    }

    public B addAll(Stream<X> elements) {
        return apply(collection -> elements.forEach(collection::add));
    }

    public X[] toArray(Class<? super X> componentType) {
        return toArray(size -> (X[]) Array.newInstance(componentType, size));
    }

    public X[] toArray(IntFunction<X[]> generator) {
        return build().stream().toArray(generator);
    }

}
