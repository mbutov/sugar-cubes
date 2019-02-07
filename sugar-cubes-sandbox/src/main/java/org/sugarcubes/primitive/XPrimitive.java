package org.sugarcubes.primitive;

import java.lang.reflect.Array;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public enum XPrimitive {

    BOOLEAN(boolean.class),
    BYTE(byte.class),
    CHARACTER(char.class),
    SHORT(short.class),
    INTEGER(int.class),
    LONG(long.class),
    FLOAT(float.class),
    DOUBLE(double.class);

    private final Class<?> wrapperType;
    private final Class<?> primitiveType;
    private final Object defaultValue;

    XPrimitive(Class<?> primitiveType) {
        this.primitiveType = primitiveType;
        this.defaultValue = Array.get(Array.newInstance(primitiveType, 1), 0);
        this.wrapperType = this.defaultValue.getClass();
    }

    public Class<?> getPrimitiveType() {
        return primitiveType;
    }

    public Class<?> getWrapperType() {
        return wrapperType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

}
