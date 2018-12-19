package org.sugarcubes.tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Factory for {@link Tuple}s and some utility methods.
 *
 * @author Maxim Butov
 */
public class Tuples {

    public static <T> Empty<T> of() {
        return Empty.INSTANCE;
    }

    public static <A> Single<A> of(A a) {
        return new Single<A>(a);
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }

    public static <A, B, C> Triplet<A, B, C> of(A a, B b, C c) {
        return new Triplet<A, B, C>(a, b, c);
    }

    public static <T> Tuple<T> of(T... values) {
        return new Tuple<T>(values);
    }

    public static <T> Tuple<T> of(Collection<T> values) {
        return new Tuple<T>(values);
    }

    public static <T> Tuple<T> union(Tuple<T> left, Tuple<T> right) {
        List<T> list = new ArrayList<>(left.size() + right.size());
        list.addAll(left);
        list.addAll(right);
        return new Tuple<T>(false, list.toArray());
    }

    public static <T> Tuple<T> append(Tuple<T> tuple, T... values) {
        return union(tuple, of(values));
    }

    public static <T> Tuple<T> prepend(Tuple<T> tuple, T... values) {
        return union(of(values), tuple);
    }

}
