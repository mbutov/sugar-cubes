package org.sugarcubes.tuple;

/**
 * Single element tuple.
 *
 * @see TupleImpl
 *
 * @author Maxim Butov
 */
public class Single<A> extends TupleImpl implements HasFisrt<A> {

    private static final long serialVersionUID = 1L;

    public Single(A a) {
        super(false, new Object[] {a});
    }

}
