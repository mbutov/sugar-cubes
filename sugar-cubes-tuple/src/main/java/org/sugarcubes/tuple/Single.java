package org.sugarcubes.tuple;

/**
 * Single element tuple.
 *
 * @see Tuple
 *
 * @author Maxim Butov
 */
public class Single<A> extends Tuple implements HasFisrt<A> {

    public Single(A a) {
        super(false, new Object[] {a});
    }

}
