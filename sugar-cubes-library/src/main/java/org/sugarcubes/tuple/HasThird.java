package org.sugarcubes.tuple;

import java.util.List;

/**
 * A mix-in for tuples with 3+ elements.
 *
 * @author Maxim Butov
 */
public interface HasThird<C> extends List {

    /**
     * @return the 3-rd element of tuple
     */
    default C getThird() {
        return (C) get(2);
    }

}
