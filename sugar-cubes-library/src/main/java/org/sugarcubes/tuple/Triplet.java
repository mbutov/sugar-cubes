package org.sugarcubes.tuple;

/**
 * Three elements tuple.
 *
 * @see TupleImpl
 *
 * @author Maxim Butov
 */
public class Triplet<A, B, C> extends TupleImpl implements HasFisrt<A>, HasSecond<B>, HasThird<C> {

    public Triplet(A a, B b, C c) {
        super(false, new Object[] {a, b, c});
    }

}
