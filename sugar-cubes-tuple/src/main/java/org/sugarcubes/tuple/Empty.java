package org.sugarcubes.tuple;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

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

    // singleton serialization stuff goes below

    private static final class Writable implements Serializable {

        private static final long serialVersionUID = 1L;

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }

        private void readObjectNoData() throws ObjectStreamException {
        }

    }

    private Object writeReplace() throws ObjectStreamException {
        return new Writable();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        throw new NotSerializableException();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new NotSerializableException();
    }

}
