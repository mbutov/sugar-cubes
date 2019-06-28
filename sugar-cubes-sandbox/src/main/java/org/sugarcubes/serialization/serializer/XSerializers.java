package org.sugarcubes.serialization.serializer;

import java.util.Map;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.serialization.XSerializer;
import org.sugarcubes.serialization.XTag;

/**
 * @author Maxim Butov
 */
public class XSerializers {

    public static final Map<XTag, XSerializer> SERIALIZERS = MapBuilder.<XTag, XSerializer>linkedHashMap()

        .errorOnDuplicateKeys()

        .put(XTag.of(0x02), new XClassSerializer())
        .put(XTag.of(0x03), new XEnumSerializer())

        .put(XTag.of(0x10), new XConstantSerializer<>(false))
        .put(XTag.of(0x11), new XConstantSerializer<>((byte) 0))
        .put(XTag.of(0x12), new XConstantSerializer<>((short) 0))
        .put(XTag.of(0x13), new XConstantSerializer<>((char) 0))
        .put(XTag.of(0x14), new XConstantSerializer<>((int) 0))
        .put(XTag.of(0x15), new XConstantSerializer<>((long) 0))
        .put(XTag.of(0x16), new XConstantSerializer<>((float) 0))
        .put(XTag.of(0x17), new XConstantSerializer<>((double) 0))

        .put(XTag.of(0x20), new XConstantSerializer<>(true))
        .put(XTag.of(0x21), new XConstantSerializer<>((byte) 1))
        .put(XTag.of(0x22), new XConstantSerializer<>((short) 1))
        .put(XTag.of(0x23), new XConstantSerializer<>((char) 1))
        .put(XTag.of(0x24), new XConstantSerializer<>((int) 1))
        .put(XTag.of(0x25), new XConstantSerializer<>((long) 1))
        .put(XTag.of(0x26), new XConstantSerializer<>((float) 1))
        .put(XTag.of(0x27), new XConstantSerializer<>((double) 1))

        .put(XTag.of(0x31), new XConstantSerializer<>((byte) -1))
        .put(XTag.of(0x32), new XConstantSerializer<>((short) -1))
        .put(XTag.of(0x33), new XConstantSerializer<>((char) -1))
        .put(XTag.of(0x34), new XConstantSerializer<>((int) -1))
        .put(XTag.of(0x35), new XConstantSerializer<>((long) -1))
        .put(XTag.of(0x36), new XConstantSerializer<>((float) -1))
        .put(XTag.of(0x37), new XConstantSerializer<>((double) -1))

        .put(XTag.of(0x40), new XConstantSerializer<>(""))
        .put(XTag.of(0x41), new XStringSerializer())

        .put(XTag.of(0x50), new XPrimitiveSerializer<>(Boolean.class, ValueWriter.BOOLEAN, ValueReader.BOOLEAN))
        .put(XTag.of(0x51), new XPrimitiveSerializer<>(Byte.class, ValueWriter.BYTE, ValueReader.BYTE))
        .put(XTag.of(0x52), new XPrimitiveSerializer<>(Short.class, ValueWriter.SHORT, ValueReader.SHORT))
        .put(XTag.of(0x53), new XPrimitiveSerializer<>(Character.class, ValueWriter.CHARACTER, ValueReader.CHARACTER))
        .put(XTag.of(0x54), new XPrimitiveSerializer<>(Integer.class, ValueWriter.INTEGER, ValueReader.INTEGER))
        .put(XTag.of(0x55), new XPrimitiveSerializer<>(Long.class, ValueWriter.LONG, ValueReader.LONG))
        .put(XTag.of(0x56), new XPrimitiveSerializer<>(Float.class, ValueWriter.FLOAT, ValueReader.FLOAT))
        .put(XTag.of(0x57), new XPrimitiveSerializer<>(Double.class, ValueWriter.DOUBLE, ValueReader.DOUBLE))

        .put(XTag.of(0x60), new XPrimitiveArraySerializer<>(boolean[].class, array -> array.length, boolean[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.BOOLEAN, ValueReader.BOOLEAN))
        .put(XTag.of(0x61), new XPrimitiveArraySerializer<>(byte[].class, array -> array.length, byte[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.BYTE, ValueReader.BYTE))
        .put(XTag.of(0x62), new XPrimitiveArraySerializer<>(short[].class, array -> array.length, short[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.SHORT, ValueReader.SHORT))
        .put(XTag.of(0x63), new XPrimitiveArraySerializer<>(char[].class, array -> array.length, char[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.CHARACTER, ValueReader.CHARACTER))
        .put(XTag.of(0x64), new XPrimitiveArraySerializer<>(int[].class, array -> array.length, int[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.INTEGER, ValueReader.INTEGER))
        .put(XTag.of(0x65), new XPrimitiveArraySerializer<>(long[].class, array -> array.length, long[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.LONG, ValueReader.LONG))
        .put(XTag.of(0x66), new XPrimitiveArraySerializer<>(float[].class, array -> array.length, float[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.FLOAT, ValueReader.FLOAT))
        .put(XTag.of(0x67), new XPrimitiveArraySerializer<>(double[].class, array -> array.length, double[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.DOUBLE, ValueReader.DOUBLE))

        .put(XTag.of(0xA0), new XObjectArraySerializer())

        .put(XTag.of(0x7F), new XObjectSerializer())

        .build();

}
