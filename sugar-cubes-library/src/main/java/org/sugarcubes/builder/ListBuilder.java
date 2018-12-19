package org.sugarcubes.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Maxim Butov
 */
public class ListBuilder<X, L extends List<X>> extends CollectionBuilder<X, L> {

    public ListBuilder(L value) {
        super(value);
    }

    public ListBuilder(Supplier<L> supplier) {
        super(supplier);
    }

    public static <X, L extends List<X>> ListBuilder<X, L> list(Supplier<L> supplier) {
        return new ListBuilder<X, L>(supplier);
    }

    public static <X> ListBuilder<X, List<X>> arrayList() {
        return list(ArrayList::new);
    }

    public static <X> ListBuilder<X, List<X>> linkedList() {
        return list(LinkedList::new);
    }

}
