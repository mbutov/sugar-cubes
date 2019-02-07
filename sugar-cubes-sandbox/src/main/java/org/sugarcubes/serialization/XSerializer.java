package org.sugarcubes.serialization;

import java.io.IOException;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public interface XSerializer {

    int tag();

    default boolean matches(XObjectOutputStream out, Object value) {
        throw new UnsupportedOperationException();
    }

    default void writeTag(XObjectOutputStream out) throws IOException {
        out.writeByte(tag());
    }

    default boolean write(XObjectOutputStream out, Object value) throws IOException {
        if (matches(out, value)) {
            writeTag(out);
            writeValue(out, value);
            return true;
        }
        else {
            return false;
        }
    }

    default void writeValue(XObjectOutputStream out, Object value) throws IOException {
        throw new UnsupportedOperationException();
    }

    Object create(XObjectInputStream in) throws IOException, ClassNotFoundException;

    default void readValue(XObjectInputStream in, Object value) throws IOException, ClassNotFoundException {
    }

}
