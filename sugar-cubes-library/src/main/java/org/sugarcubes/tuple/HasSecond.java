package org.sugarcubes.tuple;

/**
 * A mix-in for tuples with 2+ elements.
 *
 * @author Maxim Butov
 */
public interface HasSecond<B> extends Tuple {

    /**
     * @return the 2-nd element of tuple
     */
    default B getSecond() {
        return (B) get(1);
    }

}
