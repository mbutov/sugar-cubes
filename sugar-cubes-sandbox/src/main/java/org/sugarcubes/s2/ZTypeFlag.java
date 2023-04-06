package org.sugarcubes.s2;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import static java.io.ObjectStreamConstants.SC_BLOCK_DATA;
import static java.io.ObjectStreamConstants.SC_ENUM;
import static java.io.ObjectStreamConstants.SC_EXTERNALIZABLE;
import static java.io.ObjectStreamConstants.SC_SERIALIZABLE;
import static java.io.ObjectStreamConstants.SC_WRITE_METHOD;

public enum ZTypeFlag {

    WRITE_METHOD(SC_WRITE_METHOD),
    BLOCK_DATA(SC_BLOCK_DATA),
    SERIALIZABLE(SC_SERIALIZABLE),
    EXTERNALIZABLE(SC_EXTERNALIZABLE),
    ENUM(SC_ENUM);

    private final byte bits;

    ZTypeFlag(byte bits) {
        this.bits = bits;
    }

    public byte getBits() {
        return bits;
    }

    public boolean is(byte bits) {
        return (this.bits & bits) != 0;
    }

    public static Set<ZTypeFlag> valueOf(byte bits) {
        Set<ZTypeFlag> flags = EnumSet.noneOf(ZTypeFlag.class);
        Arrays.stream(values()).filter(ztf -> ztf.is(bits)).forEach(flags::add);
        return flags;
    }

}
