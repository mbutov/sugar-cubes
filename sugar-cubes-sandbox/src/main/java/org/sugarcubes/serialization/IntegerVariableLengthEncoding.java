package org.sugarcubes.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * https://github.com/stoklund/varint
 *
 * @author Maxim Butov
 */
public class IntegerVariableLengthEncoding {

    /**
     * Tests the long value fits 32 bits. I.e. the value can be cast to int and back to long without data loss.
     *
     * @param value long value
     *
     * @return true if it can be represented as int
     */
    public static boolean fits32(long value) {
        return value == (int) value;
    }

    /**
     * Finds the number of significant bits of a signed integer.
     * If the highest significant bit is 1, then value is negative, and vice versa.
     *
     * @param value integer value
     *
     * @return number of significant bits
     */
    public static int numBits(int value) {
        int bits = 1;
        for (int next, prev = value; (next = prev >> 1) != prev; prev = next) {
            bits++;
        }
        return bits;
    }

    public static int numBits(long value) {
        return fits32(value) ? numBits((int) value) : 32 + numBits((int) (value >> 32));
    }

    public static void writeIntLeb128(OutputStream out, int value) throws IOException {
        writeLongLeb128(out, (long) value);
    }

    public static void writeLongLeb128(OutputStream out, long value) throws IOException {
        int numBits = numBits(value);
        int numBytes = (numBits + 6) / 7;
        byte[] bytes = new byte[numBytes];
        int numBytesMinusOne = numBytes - 1;
        for (int n = numBytesMinusOne; n >= 0; n--) {
            bytes[n] = (byte) (((int) value) & 0x7F);
            value >>= 7;
        }
        for (int n = 0; n < numBytesMinusOne; n++) {
            bytes[n] += 0x80;
        }
        out.write(bytes);
    }

    public static int readIntLeb128(InputStream in) throws IOException {
        long value = readLongLeb128(in);
        if (!fits32(value)) {
            throw new IllegalStateException("Not an int32");
        }
        return (int) value;
    }

    public static long readLongLeb128(InputStream in) throws IOException {

        int b = in.read();
        long value = (b & 0x40) == 0x40 ? -1L : 0;
        value = (value << 7) + (b & 0x7F);

        while ((b & 0x80) == 0x80) {
            b = in.read();
            value = (value << 7) + (b & 0x7F);
        }

        return value;

    }

}
