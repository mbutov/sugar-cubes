package org.sugarcubes.serialization.serializer;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ValueWriter<T> {

    ValueWriter<Boolean> BOOLEAN = DataOutputStream::writeBoolean;
    ValueWriter<Byte> BYTE = DataOutputStream::writeByte;
    ValueWriter<Short> SHORT = DataOutputStream::writeShort;
    ValueWriter<Character> CHARACTER = DataOutputStream::writeChar;
    ValueWriter<Integer> INTEGER = DataOutputStream::writeInt;
    ValueWriter<Long> LONG = DataOutputStream::writeLong;
    ValueWriter<Float> FLOAT = DataOutputStream::writeFloat;
    ValueWriter<Double> DOUBLE = DataOutputStream::writeDouble;

    void write(DataOutputStream out, T value) throws IOException;

}
