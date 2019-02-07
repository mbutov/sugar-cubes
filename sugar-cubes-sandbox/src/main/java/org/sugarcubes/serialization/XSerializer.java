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

    boolean matches(XObjectOutputStream out, Object value);

    void writeValue(XObjectOutputStream out, T value) throws IOException;

    T create(XObjectInputStream in) throws IOException, ClassNotFoundException;

    default void readValue(XObjectInputStream in, T value) throws IOException, ClassNotFoundException {
    }

}
