package org.sugarcubes.primitive;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.sugarcubes.function.TernaryConsumer;

/**
 * @author Maxim Butov
 */
public class XPrimitive<W, A> {

    private final Function<A, Integer> arrayLength;
    private final Function<Integer, A> arrayFactory;
    private final BiFunction<A, Integer, W> arrayGet;
    private final TernaryConsumer<A, Integer, W> arraySet;

    private final Class<W> primitiveType;
    private final Class<A> arrayType;
    private final Class<W> wrapperType;
    private final W defaultValue;

    public XPrimitive(Function<A, Integer> arrayLength, Function<Integer, A> arrayFactory, BiFunction<A, Integer, W> arrayGet, TernaryConsumer<A, Integer, W> arraySet) {

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

}
