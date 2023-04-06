package org.sugarcubes.serialization;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public class Utf8Utils {

    private static int nextByte(InputStream input) throws IOException {
        int b = input.read();
        if (b == -1) {
            throw new EOFException("Unexpected EOF");
        }
        return b;
    }

    public static char readUtf8Char(InputStream input) throws IOException {
        int b0 = nextByte(input);
        // UTF-8: [0xxx xxxx]
        if ((b0 & 0x80) == 0) {
            return (char) b0;
        }
        // UTF-8: [110y yyyy] [10xx xxxx]
        if ((b0 & 0xE0) == 0xC0 && (b0 & 0x1E) != 0) {
            int b1 = nextByte(input);
            return (char) (((b0 << 6) & 0x07C0) | (b1 & 0x003F));
        }
        // UTF-8: [1110 zzzz] [10yy yyyy] [10xx xxxx]
        if ((b0 & 0xF0) == 0xE0) {
            int b1 = nextByte(input);
            if ((b1 & 0xC0) != 0x80 || (b0 == 0xED && b1 >= 0xA0) || ((b0 & 0x0F) == 0 && (b1 & 0x20) == 0)) {
                throw new IllegalStateException("Unsupported character");
            }
            int b2 = nextByte(input);
            if ((b2 & 0xC0) != 0x80) {
                throw new IllegalStateException("Unsupported character");
            }
            return (char) (((b0 << 12) & 0xF000) | ((b1 << 6) & 0x0FC0) | (b2 & 0x003F));
        }
        throw new IllegalStateException("Unsupported character");
    }

    public static void writeUtf8Char(OutputStream output, char c) throws IOException {
        byte[] bytes = StandardCharsets.UTF_8.encode(CharBuffer.wrap(new char[] {c})).array();
        output.write(bytes);
    }

}
