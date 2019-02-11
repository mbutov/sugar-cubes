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

    public static final int NULL = 'N';
    public static final int REFERENCE = 'R';

    public static final Map<Integer, XSerializer> SERIALIZERS = MapBuilder.<Integer, XSerializer>linkedHashMap()

        .put((int) 'F', new XConstantSerializer<>(false))
        .put((int) 'T', new XConstantSerializer<>(true))

        .put((int) 'Z', new XClassSerializer())
        .put((int) 'E', new XEnumSerializer())

        .put((int) 'b', new XPrimitiveSerializer<>(Byte.class, (out, value) -> out.writeByte(value), DataInputStream::readByte))
        .put((int) 's', new XPrimitiveSerializer<>(Short.class, (out, value) -> out.writeShort(value), DataInputStream::readShort))
        .put((int) 'c', new XPrimitiveSerializer<>(Character.class, (out, value) -> out.writeChar(value), DataInputStream::readChar))
        .put((int) 'i', new XPrimitiveSerializer<>(Integer.class, DataOutputStream::writeInt, DataInputStream::readInt))
        .put((int) 'l', new XPrimitiveSerializer<>(Long.class, DataOutputStream::writeLong, DataInputStream::readLong))
        .put((int) 'f', new XPrimitiveSerializer<>(Float.class, DataOutputStream::writeFloat, DataInputStream::readFloat))
        .put((int) 'd', new XPrimitiveSerializer<>(Double.class, DataOutputStream::writeDouble, DataInputStream::readDouble))

        .put((int) 'B', new XPrimitiveArraySerializer<>(byte[].class, array -> array.length, byte[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeByte(value), DataInputStream::readByte))
        .put((int) 'S', new XPrimitiveArraySerializer<>(short[].class, array -> array.length, short[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeShort(value), DataInputStream::readShort))
        .put((int) 'C', new XPrimitiveArraySerializer<>(char[].class, array -> array.length, char[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, (out, value) -> out.writeChar(value), DataInputStream::readChar))
        .put((int) 'I', new XPrimitiveArraySerializer<>(int[].class, array -> array.length, int[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeInt, DataInputStream::readInt))
        .put((int) 'L', new XPrimitiveArraySerializer<>(long[].class, array -> array.length, long[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeLong, DataInputStream::readLong))
        .put((int) 'F', new XPrimitiveArraySerializer<>(float[].class, array -> array.length, float[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeFloat, DataInputStream::readFloat))
        .put((int) 'D', new XPrimitiveArraySerializer<>(double[].class, array -> array.length, double[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, DataOutputStream::writeDouble, DataInputStream::readDouble))

        .put((int) 'X', new XStringSerializer())
        .put((int) 'A', new XObjectArraySerializer())

        .put((int) 'O', new XObjectSerializer())

        .build();

}
