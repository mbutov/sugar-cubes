package org.sugarcubes.tuple;

/**
 * Empty tuple.
 *
 * Empty tuple is a singleton.
 *
 * @author Maxim Butov
 */
public final class Empty<T> extends Tuple<T> {

    /**
     * An untyped instance of {@link Empty}.
     */
    public static final Empty INSTANCE = new Empty();

    /**
     * A typed instance of {@link Empty}.
     */
    public static <T> Empty<T> instance() {
        return INSTANCE;
    }

    /**
     * Constructor.
     */
    private Empty() {
        super(false, new Object[0]);
    }

}
