package org.sugarcubes.tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Factory for {@link Tuple}s and some utility methods.
 *
 * @author Maxim Butov
 */
public class Tuples {

    public static <T> Empty<T> of() {
        return Empty.instance();
    }

    public static <A> Single<A> of(A a) {
        return new Single<>(a);
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public static <A, B, C> Triplet<A, B, C> of(A a, B b, C c) {
        return new Triplet<>(a, b, c);
    }

    public static <T> Tuple<T> of(T... values) {
        return new TupleImpl<>(values);
    }

    public static <T> Tuple<T> of(Collection<T> values) {
        return new TupleImpl<>(values);
    }

    public static <T> Tuple<T> union(Tuple<T> left, Tuple<T> right) {
        return new TupleImpl<>(false, Stream.of(left, right).flatMap(Collection::stream).toArray());
    }

    public static <T> Tuple<T> append(Tuple<T> tuple, T... values) {
        return union(tuple, of(values));
    }

    public static <T> Tuple<T> prepend(Tuple<T> tuple, T... values) {
        return union(of(values), tuple);
    }

    private static final Collector<Object, List, Tuple> COLLECTOR = Collector.of(
        ArrayList::new,
        List::add,
        (List list1, List list2) -> {
            list1.addAll(list2);
            return list1;
        },
        TupleImpl::new
    );

    public static <X> Collector<X, List<X>, Tuple<X>> collector() {
        return (Collector) COLLECTOR;
    }

}
