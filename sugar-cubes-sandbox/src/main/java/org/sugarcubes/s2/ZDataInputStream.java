package org.sugarcubes.s2;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UTFDataFormatException;
import java.util.Arrays;

public class ZDataInputStream extends PushbackInputStream implements DataInput {

    public ZDataInputStream(InputStream in) {
        super(in);
    }

    public byte[] readBytesExact(int n) throws IOException {
        byte[] bytes = new byte[n];
        readFully(bytes, 0, n);
        return bytes;
    }

    public byte[] readBytesAtMost(int n) throws IOException {
        byte[] bytes = new byte[n];
        int count;
        for (count = 0; count < n; ) {
            int next = read(bytes, count, n - count);
            if (next == 0) {
                break;
            }
            count += next;
        }
        return count == n ? bytes : Arrays.copyOf(bytes, count);
    }

    @Override
    public void readFully(byte[] bytes) throws IOException {
        readFully(bytes, 0, bytes.length);
    }

    @Override
    public void readFully(byte[] bytes, int off, int len) throws IOException {
        for (int count = 0; count < len; ) {
            int next = read(bytes, off + count, len - count);
            if (next == 0) {
                throw new EOFException();
            }
            count += next;
        }
    }

    @Override
    public int skipBytes(int n) throws IOException {
        int count = 0;
        for (int next; count < n && (next = (int) skip(n - count)) > 0; ) {
            count += next;
        }
        return count;
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readUnsignedByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) readUnsignedByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        int b = read();
        if (b < 0) {
            throw new EOFException();
        }
        return b;
    }

    @Override
    public short readShort() throws IOException {
        int b0 = readUnsignedByte();
        int b1 = readUnsignedByte();
        return (short) ((b0 << 8) + b1);
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return readShort() & 0xFFFF;
    }

    @Override
    public char readChar() throws IOException {
        return (char) readShort();
    }

    @Override
    public int readInt() throws IOException {
        int s0 = readShort();
        int s1 = readShort();
        return (s0 << 16) + (s1 & 0xFFFF);
    }

    @Override
    public long readLong() throws IOException {
        long i0 = readInt();
        long i1 = readInt();
        return (i0 << 32) + (i1 & 0xFFFFFFFFL);
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final String readLine() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String readUTF() throws IOException {
        return readUTF(readUnsignedShort());
    }

    protected String readUTF(long bytesCount) throws IOException {
        return readUTF(bytesCount, new StringBuilder()).toString();
    }

    private Appendable readUTF(long bytesCount, Appendable buffer) throws IOException {
        for (long index = 0; index < bytesCount; ) {

            int b0 = read();
            if (b0 == -1) {
                throw new UTFDataFormatException();
            }
            index++;

            // UTF-8:   [0xxx xxxx]
            // Unicode: [0000 0000] [0xxx xxxx]
            if ((b0 & 0x80) == 0) {
                buffer.append((char) b0);
            }

            // UTF-8:   [110y yyyy] [10xx xxxx]
            // Unicode: [0000 0yyy] [yyxx xxxx]
            else if ((b0 & 0xE0) == 0xC0) {
                if (index++ == bytesCount) {
                    throw new UTFDataFormatException();
                }
                int b1 = read();
                if (b1 == -1 || (b1 & 0xC0) != 0x80) {
                    throw new UTFDataFormatException();
                }
                buffer.append((char) (((b0 & 0x1F) << 6) | (b1 & 0x3F)));
            }

            // UTF-8:   [1110 zzzz] [10yy yyyy] [10xx xxxx]
            // Unicode: [zzzz yyyy] [yyxx xxxx]
            else if ((b0 & 0xF0) == 0xE0) {
                if (index++ == bytesCount) {
                    throw new UTFDataFormatException();
                }
                int b1 = read();
                if (b1 == -1 || (b1 & 0xC0) != 0x80) {
                    throw new UTFDataFormatException();
                }
                if (index++ == bytesCount) {
                    throw new UTFDataFormatException();
                }
                int b2 = read();
                if (b2 == -1 || (b2 & 0xC0) != 0x80) {
                    throw new UTFDataFormatException();
                }
                buffer.append((char) (((b0 << 12) & 0xF000) | ((b1 << 6) & 0x0FC0) | (b2 & 0x003F)));
            }

            // error
            else {
                throw new UTFDataFormatException();
            }
        }

        return buffer;
    }

}
