package org.sugarcubes.serialization.serializer;

import java.util.Map;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.serialization.XSerializer;

/**
 * @author Maxim Butov
 */
public class XSerializers {

    public static final char NULL = '\u0000';
    public static final char REFERENCE = '\u0001';

    public static final Map<Character, XSerializer> SERIALIZERS = MapBuilder.<Character, XSerializer>linkedHashMap()

        .put('F', new XConstantSerializer<>(false))
        .put('T', new XConstantSerializer<>(true))

        .put('Z', new XClassSerializer())
        .put('E', new XEnumSerializer())

        .put('0', new XConstantSerializer<>(0))
        .put('1', new XConstantSerializer<>(1))

        .put('b', new XPrimitiveSerializer<>(Byte.class, ValueWriter.BYTE, ValueReader.BYTE))
        .put('s', new XPrimitiveSerializer<>(Short.class, ValueWriter.SHORT, ValueReader.SHORT))
        .put('c', new XPrimitiveSerializer<>(Character.class, ValueWriter.CHARACTER, ValueReader.CHARACTER))
        .put('i', new XPrimitiveSerializer<>(Integer.class, ValueWriter.INTEGER, ValueReader.INTEGER))
        .put('l', new XPrimitiveSerializer<>(Long.class, ValueWriter.LONG, ValueReader.LONG))
        .put('f', new XPrimitiveSerializer<>(Float.class, ValueWriter.FLOAT, ValueReader.FLOAT))
        .put('d', new XPrimitiveSerializer<>(Double.class, ValueWriter.DOUBLE, ValueReader.DOUBLE))

        .put('B', new XPrimitiveArraySerializer<>(boolean[].class, array -> array.length, boolean[]::new, (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.BOOLEAN, ValueReader.BOOLEAN))
        .put('B', new XPrimitiveArraySerializer<>(byte[].class,    array -> array.length, byte[]::new,    (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.BYTE, ValueReader.BYTE))
        .put('S', new XPrimitiveArraySerializer<>(short[].class,   array -> array.length, short[]::new,   (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.SHORT, ValueReader.SHORT))
        .put('C', new XPrimitiveArraySerializer<>(char[].class,    array -> array.length, char[]::new,    (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.CHARACTER, ValueReader.CHARACTER))
        .put('I', new XPrimitiveArraySerializer<>(int[].class,     array -> array.length, int[]::new,     (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.INTEGER, ValueReader.INTEGER))
        .put('L', new XPrimitiveArraySerializer<>(long[].class,    array -> array.length, long[]::new,    (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.LONG, ValueReader.LONG))
        .put('F', new XPrimitiveArraySerializer<>(float[].class,   array -> array.length, float[]::new,   (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.FLOAT, ValueReader.FLOAT))
        .put('D', new XPrimitiveArraySerializer<>(double[].class,  array -> array.length, double[]::new,  (array, k) -> array[k], (array, k, value) -> array[k] = value, ValueWriter.DOUBLE, ValueReader.DOUBLE))

        .put('X', new XStringSerializer())
        .put('A', new XObjectArraySerializer())

        .put('O', new XObjectSerializer())

        .build();

}
