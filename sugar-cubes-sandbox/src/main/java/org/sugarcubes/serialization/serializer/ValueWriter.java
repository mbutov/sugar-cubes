package org.sugarcubes.serialization.serializer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ValueWriter<T> {

    void write(DataOutputStream out, T value) throws IOException;

}
