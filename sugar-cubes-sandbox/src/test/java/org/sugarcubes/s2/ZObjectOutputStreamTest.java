package org.sugarcubes.s2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
class ZObjectOutputStreamTest {

    @Test
    void testZOutputStream() throws IOException {
        testZOutputStream(Arrays.asList());
        testZOutputStream(Arrays.asList((Object) null));
        testZOutputStream(Arrays.asList(null, null));
    }

    void testZOutputStream(List<Object> objects) throws IOException {
        byte[] original = write(ObjectOutputStream::new, objects);
        byte[] z = write(ZObjectOutputStream::new, objects);
        Assertions.assertArrayEquals(original, z);
    }

    byte[] write(UnsafeFunction<OutputStream, ObjectOutput, IOException> stream, List<Object> objects) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput objectOutputStream = stream.apply(buffer);
        for (Object object : objects) {
            objectOutputStream.writeObject(object);
        }
        return buffer.toByteArray();
    }

    @FunctionalInterface
    interface UnsafeFunction<X, Y, E extends Throwable> {

        Y apply(X x) throws E;

    }

}
