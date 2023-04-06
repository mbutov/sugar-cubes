package org.sugarcubes.s2;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.io.ObjectStreamConstants.*;

/**
 * @author Maxim Butov
 */
public class ZObjectOutputStream extends DataOutputStream implements ObjectOutput {

    ObjectOutputStream oos;

    public ZObjectOutputStream(OutputStream out) throws IOException {
        super(out);
        writeStreamHeader();
    }

    /**
     * The writeStreamHeader method is provided so subclasses can append or
     * prepend their own header to the stream.  It writes the magic number and
     * version to the stream.
     *
     * @throws IOException if I/O errors occur while writing to the underlying
     *                     stream
     */
    protected void writeStreamHeader() throws IOException {
        writeShort(STREAM_MAGIC);
        writeShort(STREAM_VERSION);
    }

    /**
     * Writes null code to stream.
     */
    private void writeNull() throws IOException {
        writeByte(TC_NULL);
    }

    /**
     * Writes given object handle to stream.
     */
    private void writeHandle(int handle) throws IOException {
        writeByte(TC_REFERENCE);
        writeInt(baseWireHandle + handle);
    }

    /**
     * Writes given string to stream, using standard or long UTF format
     * depending on string length.
     */
    private void writeString(String str, boolean unshared) throws IOException {
        // todo: unshared
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= Short.MAX_VALUE) {
            writeByte(TC_STRING);
            writeShort(bytes.length);
            write(bytes);
        } else {
            writeByte(TC_LONGSTRING);
            writeLong(bytes.length);
            write(bytes);
        }
    }

    @Override
    public void writeObject(Object obj) throws IOException {
        writeObject(obj, false);
    }

    private void writeObject(Object obj, boolean unshared) throws IOException {
        if (obj == null) {
            writeNull();
        }
    }

}
