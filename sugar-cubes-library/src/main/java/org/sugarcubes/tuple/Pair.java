package org.sugarcubes.tuple;

/**
 * Two elements tuple.
 *
 * @see TupleImpl
 *
 * @author Maxim Butov
 */
public class Pair<A, B> extends TupleImpl implements HasFisrt<A>, HasSecond<B> {

    private static final long serialVersionUID = 1L;

    public Pair(A a, B b) {
        super(false, new Object[] {a, b});
    }

}
