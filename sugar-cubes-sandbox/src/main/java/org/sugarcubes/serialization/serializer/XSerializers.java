package org.sugarcubes.serialization.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Map;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.serialization.XSerializer;

/**
 * @author Maxim Butov
 */
public class XSerializers {

    public static final char NULL = 'N';
    public static final char REFERENCE = 'R';

    public static final Map<Character, XSerializer> SERIALIZERS = MapBuilder.<Character, XSerializer>linkedHashMap()

        .put('F', new XConstantSerializer<>(false))
        .put('T', new XConstantSerializer<>(true))

        .put('Z', new XClassSerializer())
        .put('E', new XEnumSerializer())

        .put('0', new XConstantSerializer<>(0))
        .put('1', new XConstantSerializer<>(1))

        .put('b', new XPrimitiveSerializer<>(Byte.class, (out, value) -> out.writeByte(value), DataInputStream::readByte))
        .put('s', new XPrimitiveSerializer<>(Short.class, (out, value) -> out.writeShort(value), DataInputStream::readShort))
        .put('c', new XPrimitiveSerializer<>(Character.class, (out, value) -> out.writeChar(value), DataInputStream::readChar))
        .put('i', new XPrimitiveSerializer<>(Integer.class, DataOutputStream::writeInt, DataInputStream::readInt))
        .put('l', new XPrimitiveSerializer<>(Long.class, DataOutputStream::writeLong, DataInputStream::readLong))
        .put('f', new XPrimitiveSerializer<>(Float.class, DataOutputStream::writeFloat, DataInputStream::readFloat))
        .put('d', new XPrimitiveSerializer<>(Double.class, DataOutputStream::writeDouble, DataInputStream::readDouble))

        .put('B', new XPrimitiveArraySerializer<>(byte[].class, array -> array.length, byte[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeByte(value), DataInputStream::readByte))
        .put('S', new XPrimitiveArraySerializer<>(short[].class, array -> array.length, short[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeShort(value), DataInputStream::readShort))
        .put('C', new XPrimitiveArraySerializer<>(char[].class, array -> array.length, char[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeChar(value), DataInputStream::readChar))
        .put('I', new XPrimitiveArraySerializer<>(int[].class, array -> array.length, int[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeInt, DataInputStream::readInt))
        .put('L', new XPrimitiveArraySerializer<>(long[].class, array -> array.length, long[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeLong, DataInputStream::readLong))
        .put('F', new XPrimitiveArraySerializer<>(float[].class, array -> array.length, float[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeFloat, DataInputStream::readFloat))
        .put('D', new XPrimitiveArraySerializer<>(double[].class, array -> array.length, double[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeDouble, DataInputStream::readDouble))

        .put('X', new XStringSerializer())
        .put('A', new XObjectArraySerializer())

        .put('O', new XObjectSerializer())

        .build();

}
