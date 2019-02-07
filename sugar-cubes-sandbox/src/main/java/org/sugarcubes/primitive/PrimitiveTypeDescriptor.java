package org.sugarcubes.primitive;

import java.lang.reflect.Array;

import org.sugarcubes.reflection.XReflection;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class PrimitiveTypeDescriptor<T> {

    private final Class<T> wrapperType;
    private final Class<?> primitiveType;
    private final T defaultValue;

    PrimitiveTypeDescriptor(Class<T> wrapperType) {
        this(wrapperType, getPrimitiveType(wrapperType));
    }

    PrimitiveTypeDescriptor(Class<T> wrapperType, Class<?> primitiveType) {
        this(wrapperType, primitiveType, (T) getDefaultValue(primitiveType));
    }

    PrimitiveTypeDescriptor(Class<T> wrapperType, Class<?> primitiveType, T defaultValue) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.defaultValue = defaultValue;
    }

    public Class<T> getWrapperType() {
        return wrapperType;
    }

    public Class<?> getPrimitiveType() {
        return primitiveType;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    private static Class<?> getPrimitiveType(Class<?> wrapperType) {
        return XReflection.of(wrapperType).<Class<?>>getDeclaredField("TYPE").get(null);
    }

    private static Object getDefaultValue(Class<?> primitiveType) {
        return Array.get(Array.newInstance(primitiveType, 1), 0);
    }

}
