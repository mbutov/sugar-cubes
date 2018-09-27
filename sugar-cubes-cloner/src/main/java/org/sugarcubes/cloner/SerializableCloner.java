package org.sugarcubes.cloner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The implementation of {@link Cloner} which uses Java serialization for cloning.
 *
 * @author Maxim Butov
 */
public class SerializableCloner extends AbstractCloner {

    /**
     * Singleton instance of the cloner.
     */
    public static final Cloner INSTANCE = new SerializableCloner();

    /**
     * A faster implementation.
     */
    private static class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {

        private ByteArrayOutputStream() {
            super(0x1000); // 4K buffer
        }

        private byte[] buf() {
            return buf;
        }

    }

    @Override
    protected Object doClone(Object object) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new ObjectOutputStream(out).writeObject(object);
            return new ObjectInputStream(new ByteArrayInputStream(out.buf(), 0, out.size())).readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            throw new ClonerException(e);
        }
    }

}
