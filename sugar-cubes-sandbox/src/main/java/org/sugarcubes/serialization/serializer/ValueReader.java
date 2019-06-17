package org.sugarcubes.serialization.serializer;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
@FunctionalInterface
public interface ValueReader<T> {

    ValueReader<Boolean> BOOLEAN = DataInputStream::readBoolean;
    ValueReader<Byte> BYTE = DataInputStream::readByte;
    ValueReader<Short> SHORT = DataInputStream::readShort;
    ValueReader<Character> CHARACTER = DataInputStream::readChar;
    ValueReader<Integer> INTEGER = DataInputStream::readInt;
    ValueReader<Long> LONG = DataInputStream::readLong;
    ValueReader<Float> FLOAT = DataInputStream::readFloat;
    ValueReader<Double> DOUBLE = DataInputStream::readDouble;

    T read(DataInputStream in) throws IOException;

}
