package org.sugarcubes.tuple;

/**
 * Empty tuple.
 *
 * Empty tuple is a singleton.
 *
 * @author Maxim Butov
 */
public final class Empty<T> extends TupleImpl<T> {

    private static final long serialVersionUID = 1L;

    /**
     * An untyped instance of {@link Empty}.
     */
    public static final Empty<?> INSTANCE = new Empty<>();

    /**
     * A typed instance of {@link Empty}.
     */
    public static <T> Empty<T> instance() {
        return (Empty) INSTANCE;
    }

    /**
     * Constructor.
     */
    private Empty() {
        super(false, new Object[0]);
    }

    private Object readResolve() {
        return INSTANCE;
    }

}
