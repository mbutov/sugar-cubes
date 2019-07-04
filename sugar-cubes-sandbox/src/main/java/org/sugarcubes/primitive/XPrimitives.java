package org.sugarcubes.primitive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * Enumeration of {@link XPrimitive}s/{@link XPrimitiveNumber}s.
 *
 * @author Maxim Butov
 */
public class XPrimitives {

    public static final XPrimitive<Boolean, boolean[]> BOOLEAN = new XBoolean();
    public static final XPrimitive<Character, char[]> CHARACTER = new XCharacter();
    public static final XPrimitiveNumber<Byte, byte[]> BYTE = new XByte();
    public static final XPrimitiveNumber<Short, short[]> SHORT = new XShort();
    public static final XPrimitiveNumber<Integer, int[]> INTEGER = new XInteger();
    public static final XPrimitiveNumber<Long, long[]> LONG = new XLong();
    public static final XPrimitiveNumber<Float, float[]> FLOAT = new XFloat();
    public static final XPrimitiveNumber<Double, double[]> DOUBLE = new XDouble();

    public static final Set<XPrimitive<?, ?>> PRIMITIVES =
        SetBuilder.unmodifiableLinkedHashSet(BOOLEAN, BYTE, SHORT, CHARACTER, INTEGER, LONG, FLOAT, DOUBLE);

    public static final Set<XPrimitiveNumber<?, ?>> NUMBERS =
        SetBuilder.unmodifiableLinkedHashSet(BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE);

    public static final Map<Class<?>, XPrimitive> PRIMITIVES_BY_TYPE = MapBuilder.<Class<?>, XPrimitive>hashMap()
        .putAll(PRIMITIVES.stream().collect(Collectors.toMap(XPrimitive::getPrimitiveType, Function.identity())))
        .putAll(PRIMITIVES.stream().collect(Collectors.toMap(XPrimitive::getWrapperType, Function.identity())))
        .transform(Collections::unmodifiableMap)
        .build();

    public static <W, A> XPrimitive<W, A> getPrimitive(Class<W> type) {
        return PRIMITIVES_BY_TYPE.get(type);
    }

    public static <W extends Number, A> XPrimitiveNumber<W, A> getNumber(Class<W> type) {
        return (XPrimitiveNumber<W, A>) getPrimitive(type);
    }

    private static class XBoolean extends XPrimitive<Boolean, boolean[]> {

        @Override
        public boolean[] newArray(int length) {
            return new boolean[length];
        }

        @Override
        public int length(boolean[] array) {
            return array.length;
        }

        @Override
        public Boolean get(boolean[] array, int index) {
            return array[index];
        }

        @Override
        public void set(boolean[] array, int index, Boolean value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Boolean value) throws IOException {
            out.writeBoolean(value);
        }

        @Override
        public Boolean read(DataInputStream in) throws IOException {
            return in.readBoolean();
        }

    }

    private static class XCharacter extends XPrimitive<Character, char[]> {

        @Override
        public char[] newArray(int length) {
            return new char[length];
        }

        @Override
        public int length(char[] array) {
            return array.length;
        }

        @Override
        public Character get(char[] array, int index) {
            return array[index];
        }

        @Override
        public void set(char[] array, int index, Character value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Character value) throws IOException {
            out.writeChar(value);
        }

        @Override
        public Character read(DataInputStream in) throws IOException {
            return in.readChar();
        }

    }

    private static class XByte extends XPrimitiveNumber<Byte, byte[]> {

        @Override
        public byte[] newArray(int length) {
            return new byte[length];
        }

        @Override
        public int length(byte[] array) {
            return array.length;
        }

        @Override
        public Byte get(byte[] array, int index) {
            return array[index];
        }

        @Override
        public void set(byte[] array, int index, Byte value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Byte value) throws IOException {
            out.writeByte(value);
        }

        @Override
        public Byte read(DataInputStream in) throws IOException {
            return in.readByte();
        }

        @Override
        public Byte cast(Number value) {
            return value.byteValue();
        }

    }

    private static class XShort extends XPrimitiveNumber<Short, short[]> {

        @Override
        public short[] newArray(int length) {
            return new short[length];
        }

        @Override
        public int length(short[] array) {
            return array.length;
        }

        @Override
        public Short get(short[] array, int index) {
            return array[index];
        }

        @Override
        public void set(short[] array, int index, Short value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Short value) throws IOException {
            out.writeShort(value);
        }

        @Override
        public Short read(DataInputStream in) throws IOException {
            return in.readShort();
        }

        @Override
        public Short cast(Number value) {
            return value.shortValue();
        }

    }

    private static class XInteger extends XPrimitiveNumber<Integer, int[]> {

        @Override
        public int[] newArray(int length) {
            return new int[length];
        }

        @Override
        public int length(int[] array) {
            return array.length;
        }

        @Override
        public Integer get(int[] array, int index) {
            return array[index];
        }

        @Override
        public void set(int[] array, int index, Integer value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Integer value) throws IOException {
            out.writeInt(value);
        }

        @Override
        public Integer read(DataInputStream in) throws IOException {
            return in.readInt();
        }

        @Override
        public Integer cast(Number value) {
            return value.intValue();
        }

    }

    private static class XLong extends XPrimitiveNumber<Long, long[]> {

        @Override
        public long[] newArray(int length) {
            return new long[length];
        }

        @Override
        public int length(long[] array) {
            return array.length;
        }

        @Override
        public Long get(long[] array, int index) {
            return array[index];
        }

        @Override
        public void set(long[] array, int index, Long value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Long value) throws IOException {
            out.writeLong(value);
        }

        @Override
        public Long read(DataInputStream in) throws IOException {
            return in.readLong();
        }

        @Override
        public Long cast(Number value) {
            return value.longValue();
        }

    }

    private static class XFloat extends XPrimitiveNumber<Float, float[]> {

        @Override
        public float[] newArray(int length) {
            return new float[length];
        }

        @Override
        public int length(float[] array) {
            return array.length;
        }

        @Override
        public Float get(float[] array, int index) {
            return array[index];
        }

        @Override
        public void set(float[] array, int index, Float value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Float value) throws IOException {
            out.writeFloat(value);
        }

        @Override
        public Float read(DataInputStream in) throws IOException {
            return in.readFloat();
        }

        @Override
        public Float cast(Number value) {
            return value.floatValue();
        }

    }

    private static class XDouble extends XPrimitiveNumber<Double, double[]> {

        @Override
        public double[] newArray(int length) {
            return new double[length];
        }

        @Override
        public int length(double[] array) {
            return array.length;
        }

        @Override
        public Double get(double[] array, int index) {
            return array[index];
        }

        @Override
        public void set(double[] array, int index, Double value) {
            array[index] = value;
        }

        @Override
        public void write(DataOutputStream out, Double value) throws IOException {
            out.writeDouble(value);
        }

        @Override
        public Double read(DataInputStream in) throws IOException {
            return in.readDouble();
        }

        @Override
        public Double cast(Number value) {
            return value.doubleValue();
        }

    }

}
