package org.sugarcubes.primitive;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.sugarcubes.function.TernaryConsumer;

/**
 * @author Maxim Butov
 */
public class XPrimitiveType<W, A> {

    public static final XPrimitiveType<Boolean, boolean[]> BOOLEAN = new XPrimitiveType<>(
        array -> array.length,
        boolean[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Byte, byte[]> BYTE = new XPrimitiveType<>(
        array -> array.length,
        byte[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Short, short[]> SHORT = new XPrimitiveType<>(
        array -> array.length,
        short[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Character, char[]> CHARACTER = new XPrimitiveType<>(
        array -> array.length,
        char[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Integer, int[]> INTEGER = new XPrimitiveType<>(
        array -> array.length,
        int[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Long, long[]> LONG = new XPrimitiveType<>(
        array -> array.length,
        long[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Float, float[]> FLOAT = new XPrimitiveType<>(
        array -> array.length,
        float[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    public static final XPrimitiveType<Double, double[]> DOUBLE = new XPrimitiveType<>(
        array -> array.length,
        double[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value);

    private final Function<A, Integer> arrayLength;
    private final Function<Integer, A> arrayFactory;
    private final BiFunction<A, Integer, W> arrayGet;
    private final TernaryConsumer<A, Integer, W> arraySet;

    private final Class<W> primitiveType;
    private final Class<A> arrayType;
    private final Class<W> wrapperType;
    private final W defaultValue;

    public XPrimitiveType(Function<A, Integer> arrayLength, Function<Integer, A> arrayFactory, BiFunction<A, Integer, W> arrayGet, TernaryConsumer<A, Integer, W> arraySet) {

        this.arrayLength = arrayLength;
        this.arrayFactory = arrayFactory;
        this.arrayGet = arrayGet;
        this.arraySet = arraySet;

        A array = arrayFactory.apply(1);

        this.arrayType = (Class) array.getClass();
        this.primitiveType = (Class) this.arrayType.getComponentType();
        this.defaultValue = this.arrayGet.apply(array, 0);
        this.wrapperType = (Class) this.defaultValue.getClass();
    }

    public Class<W> getPrimitiveType() {
        return primitiveType;
    }

    public Class<A> getArrayType() {
        return arrayType;
    }

    public Class<W> getWrapperType() {
        return wrapperType;
    }

    public W getDefaultValue() {
        return defaultValue;
    }

    public int length(A array) {
        return arrayLength.apply(array);
    }

    public A newArray(int length) {
        return arrayFactory.apply(length);
    }

    public W get(A array, int index) {
        return arrayGet.apply(array, index);
    }

    public void set(A array, int index, W value) {
        arraySet.accept(array, index, value);
    }

    public static void main(String[] args) {
        long x = Long.MAX_VALUE;
        float y = x;
        long z = (long) y;
        System.out.println(x==z);
    }

}
