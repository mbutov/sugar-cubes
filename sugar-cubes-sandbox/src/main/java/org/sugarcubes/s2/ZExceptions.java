package org.sugarcubes.s2;

import java.io.IOException;
import java.io.StreamCorruptedException;

class ZExceptions {

    static IOException invalid(String what, Object obj) throws IOException {
        throw new StreamCorruptedException(String.format("Invalid %s: %s.", what, obj));
    }

    static IOException invalid(String what, short s) throws IOException {
        throw new StreamCorruptedException(String.format("Invalid %s: 0x%04X.", what, s));
    }

    static IOException invalid(String what, byte b) throws IOException {
        throw new StreamCorruptedException(String.format("Invalid %s: 0x%02X.", what, b));
    }

}
