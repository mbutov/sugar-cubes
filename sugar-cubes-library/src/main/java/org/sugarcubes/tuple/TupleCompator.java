package org.sugarcubes.tuple;

import java.util.Comparator;

/**
 * Tuple comparator for lexicographical order.
 *
 * @author Maxim Butov
 */
public class TupleCompator<T> implements Comparator<Tuple<T>> {

    public static final TupleCompator NATURAL_ORDER = new TupleCompator((Comparator) Comparator.naturalOrder());

    public static <T extends Comparable> TupleCompator<T> naturalOrder() {
        return NATURAL_ORDER;
    }

    private final Comparator<T> comparator;

    /**
     * Creates tuple comparator on the base of element comparator.
     *
     * @param comparator tuple element comparator
     */
    public TupleCompator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(Tuple<T> tuple1, Tuple<T> tuple2) {

        int size1 = tuple1.size();
        int size2 = tuple2.size();

        int minSize = Math.min(size1, size2);

        for (int index = 0; index < minSize; index++) {
            int result = comparator.compare(tuple1.get(index), tuple2.get(index));
            if (result != 0) {
                return result;
            }
        }

        return Integer.compare(size1, size2);

    }

}
