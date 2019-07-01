package org.sugarcubes.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Maxim Butov
 */
public class IntegerVariableLengthEncoding {

    public static boolean fits32(long value) {
        return value == (int) value;
    }

    public static int numBits(int value) {
        int bits = 1;
        while (value != 0 && value != -1) {
            bits++;
            value >>= 1;
        }
        return bits;
    }

    public static int numBits(long value) {
        return fits32(value) ? numBits((int) value) : 32 + numBits((int) (value >> 32));
    }

    public static void writeInt(OutputStream out, int value) throws IOException {
        writeLong(out, (long) value);
    }

    public static void writeLong(OutputStream out, long value) throws IOException {
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

    public static int readInt(InputStream in) throws IOException {
        long value = readLong(in);
        if (!fits32(value)) {
            throw new IllegalStateException("Not an int32");
        }
        return (int) value;
    }

    public static long readLong(InputStream in) throws IOException {

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
