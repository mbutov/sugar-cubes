package org.sugarcubes.serialization.serializer;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ValueReader<T> {

    T read(DataInputStream in) throws IOException;

}
