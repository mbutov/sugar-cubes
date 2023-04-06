package org.sugarcubes.tuple;

import java.util.Map;

/**
 * Two elements tuple.
 *
 * @see TupleImpl
 *
 * @author Maxim Butov
 */
public class Pair<A, B> extends TupleImpl implements HasFisrt<A>, HasSecond<B>, Map.Entry<A, B> {

    private static final long serialVersionUID = 1L;

    public Pair(A a, B b) {
        super(false, new Object[] {a, b});
    }

    @Override
    public int compareTo(Object that) {
        return compareTo((Tuple) that);
    }

    @Override
    public A getKey() {
        return getFirst();
    }

    @Override
    public B getValue() {
        return getSecond();
    }

    @Override
    public B setValue(B value) {
        throw new UnsupportedOperationException();
    }
}
