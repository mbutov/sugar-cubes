package org.sugarcubes.tuple;

import java.util.List;

/**
 * A mix-in for tuples with 1+ elements.
 *
 * @author Maxim Butov
 */
public interface HasFisrt<A> extends List {

    /**
     * @return the 1-st element of tuple
     */
    default A getFirst() {
        return (A) get(0);
    }

}
