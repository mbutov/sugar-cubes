package org.sugarcubes.serialization;

import java.io.IOException;

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
