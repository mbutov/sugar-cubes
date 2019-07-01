package org.sugarcubes.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.sugarcubes.check.Args;
import org.sugarcubes.check.Checks;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public class XTag {

    /**
     * Tag for references.
     */
    public static final XTag NULL = new XTag(0);

    /**
     * Tag for references.
     */
    public static final XTag REFERENCE = new XTag(1);

    public static XTag of(int value) {

        Args.check(value > 1, Checks.format("Value must be > 1"));
        Args.check(value < WORD_WITH_HIGH_BIT, Checks.format("Value must be < 0x8000"));

        return new XTag(value);
    }

    // [0xxx xxxx] 7
    // [100x xxxx xxxx xxxx] 13
    // [101x xxxx xxxx xxxx xxxx xxxx] 21
    // [11xx xxxx xxxx xxxx xxxx xxxx xxxx xxxx] 30

    /**
     * Value in range [0, 0x8000).
     */
    private final int value;

    private static final int BYTE_WITH_HIGH_BIT = 0x0080;
    private static final int WORD_WITH_HIGH_BIT = 0x8000;

    /* test */ XTag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XTag)) {
            return false;
        }
        XTag that = (XTag) obj;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return "XTag[0x" + Integer.toHexString(value) + "]";
    }

    public void write(OutputStream output) throws IOException {
        if (value < BYTE_WITH_HIGH_BIT) {
            output.write(value);
        }
        else {
            output.write(((value >> 8) & 0x7F) + BYTE_WITH_HIGH_BIT);
            output.write(value & 0xFF);
        }
    }

    public static XTag read(InputStream input) throws IOException {
        int b0 = readAndCheckEof(input);
        if (b0 < BYTE_WITH_HIGH_BIT) {
            return new XTag(b0);
        }
        else {
            int b1 = readAndCheckEof(input);
            return new XTag(((b0 & 0x7F) << 8) + b1);
        }
    }

    private static int readAndCheckEof(InputStream input) throws IOException {
        int b = input.read();
        if (b < 0) {
            throw new IllegalStateException("Unexpected EOF");
        }
        return b;
    }

}
