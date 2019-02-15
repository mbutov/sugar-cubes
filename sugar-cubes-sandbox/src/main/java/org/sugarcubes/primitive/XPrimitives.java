package org.sugarcubes.primitive;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitives {

    public static final XPrimitive<Boolean, boolean[]> BOOLEAN = new XPrimitive<>(
        array -> array.length,
        boolean[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value
    );

    public static final XPrimitive<Character, char[]> CHARACTER = new XPrimitive<>(
        array -> array.length,
        char[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value
    );

    public static final XPrimitiveNumber<Byte, byte[]> BYTE = new XPrimitiveNumber<>(
        array -> array.length,
        byte[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::byteValue
    );

    public static final XPrimitiveNumber<Short, short[]> SHORT = new XPrimitiveNumber<>(
        array -> array.length,
        short[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::shortValue
    );

    public static final XPrimitiveNumber<Integer, int[]> INTEGER = new XPrimitiveNumber<>(
        array -> array.length,
        int[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::intValue
    );

    public static final XPrimitiveNumber<Long, long[]> LONG = new XPrimitiveNumber<>(
        array -> array.length,
        long[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::longValue
    );

    public static final XPrimitiveNumber<Float, float[]> FLOAT = new XPrimitiveNumber<>(
        array -> array.length,
        float[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::floatValue
    );

    public static final XPrimitiveNumber<Double, double[]> DOUBLE = new XPrimitiveNumber<>(
        array -> array.length,
        double[]::new,
        (array, index) -> array[index],
        (array, index, value) -> array[index] = value,
        Number::doubleValue
    );

    public static final Set<XPrimitive<?, ?>> PRIMITIVES =
        SetBuilder.unmodifiableLinkedHashSet(BOOLEAN, BYTE, SHORT, CHARACTER, INTEGER, LONG, FLOAT, DOUBLE);

    public static final Set<XPrimitiveNumber<?, ?>> NUMBERS =
        SetBuilder.unmodifiableLinkedHashSet(BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE);

    public static final Map<Class<?>, XPrimitive> PRIMITIVES_BY_TYPE = MapBuilder.<Class<?>, XPrimitive>hashMap()
        .putAll(PRIMITIVES.stream().collect(Collectors.toMap(XPrimitive::getPrimitiveType, Function.identity())))
        .putAll(PRIMITIVES.stream().collect(Collectors.toMap(XPrimitive::getWrapperType, Function.identity())))
        .transform(Collections::unmodifiableMap)
        .build();

}
