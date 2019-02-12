package org.sugarcubes.primitive;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.sugarcubes.arg.Arg;
import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public enum XPrimitive {

    BOOLEAN(boolean.class, Function.identity()) {
        @Override
        public Object cast(Object value) {
            boolean b = (Boolean) value;
            return b;
        }
    },
    BYTE(byte.class, Number::byteValue),
    SHORT(short.class, Number::shortValue),
    CHARACTER(char.class, Function.identity()) {
        @Override
        public Object cast(Object value) {
            return (char) toNumber(value).intValue();
        }
    },
    INTEGER(int.class, Number::intValue),
    LONG(long.class, Number::longValue),
    FLOAT(float.class, Number::floatValue),
    DOUBLE(double.class, Number::doubleValue);

    public static final Set<XPrimitive> NUMBERS =
        SetBuilder.unmodifiableHashSet(BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE);

    private static final Map<Class<?>, XPrimitive> PRIMITIVES = MapBuilder.<Class<?>, XPrimitive>hashMap()
        .putAll(Arrays.stream(values()).collect(Collectors.toMap(XPrimitive::getPrimitiveType, Function.identity())))
        .putAll(Arrays.stream(values()).collect(Collectors.toMap(XPrimitive::getWrapperType, Function.identity())))
        .transform(Collections::unmodifiableMap)
        .build();

    public static XPrimitive of(Class<?> type) {
        return PRIMITIVES.get(type);
    }

    private final Class<?> wrapperType;
    private final Class<?> primitiveType;
    private final Class<?> arrayType;
    private final Object defaultValue;
    private final Function<Number, Number> fromNumber;

    XPrimitive(Class<?> primitiveType, Function<Number, Number> fromNumber) {
        Arg.check(primitiveType, Class::isPrimitive, "%s is not primitive", primitiveType);
        this.primitiveType = primitiveType;
        Object array = Array.newInstance(primitiveType, 1);
        this.arrayType = array.getClass();
        this.defaultValue = Array.get(array, 0);
        this.wrapperType = this.defaultValue.getClass();
        this.fromNumber = fromNumber;
    }

    public Class<?> getPrimitiveType() {
        return primitiveType;
    }

    public Class<?> getArrayType() {
        return arrayType;
    }

    public Class<?> getWrapperType() {
        return wrapperType;
    }

    public boolean is(Class<?> primitiveOrWrapperType) {
        return primitiveType.equals(primitiveOrWrapperType) || wrapperType.equals(primitiveOrWrapperType);
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    private static Number toNumber(Object value) {
        if (value instanceof Character) {
            return (int) ((Character) value);
        }
        else {
            return (Number) value;
        }
    }

    public Object cast(Object value) {
        return fromNumber.apply(toNumber(value));
    }

}
