package org.sugarcubes.builder.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * List builders.
 *
 * @author Maxim Butov
 */
public class ListBuilder<X, L extends List<X>> extends CollectionBuilder<X, L, ListBuilder<X, L>> {

    public ListBuilder(Supplier<L> supplier) {
        super(supplier);
    }

    public List<X> unmodifiable() {
        return transform(Collections::unmodifiableList).build();
    }

    public static <X, L extends List<X>> ListBuilder<X, L> list(Supplier<L> supplier) {
        return new ListBuilder<>(supplier);
    }

    public static <X> ListBuilder<X, List<X>> arrayList() {
        return list(ArrayList::new);
    }

    public static <X> ListBuilder<X, List<X>> linkedList() {
        return list(LinkedList::new);
    }

    public static <X> List<X> unmodifiableArrayList(X... elements) {
        return ListBuilder.<X>arrayList().addAll(elements).unmodifiable();
    }

    public static <X> List<X> unmodifiableLinkedList(X... elements) {
        return ListBuilder.<X>linkedList().addAll(elements).unmodifiable();
    }

}
