package org.sugarcubes.tuple;

/**
 * A mix-in for tuples with 1+ elements.
 *
 * @author Maxim Butov
 */
public interface HasFisrt<A> extends Tuple {

    /**
     * @return the 1-st element of tuple
     */
    default A getFirst() {
        return (A) get(0);
    }

}
