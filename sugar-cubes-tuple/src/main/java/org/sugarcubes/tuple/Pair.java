package org.sugarcubes.tuple;

/**
 * Two elements tuple.
 *
 * @see Tuple
 *
 * @author Maxim Butov
 */
public class Pair<A, B> extends Tuple implements HasFisrt<A>, HasSecond<B> {

    public Pair(A a, B b) {
        super(false, new Object[] {a, b});
    }

}
