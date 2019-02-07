package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public interface XSerializer<T> {

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
            writeValue(out, (T) value);
            return true;
        }
        else {
            return false;
        }
    }

    default void writeValue(XObjectOutputStream out, T value) throws IOException {
        throw new UnsupportedOperationException();
    }

    T create(XObjectInputStream in) throws IOException, ClassNotFoundException;

    default void readValue(XObjectInputStream in, T value) throws IOException, ClassNotFoundException {
    }

}
