package org.sugarcubes.primitive;

import java.lang.reflect.Array;
import java.util.Set;

import org.sugarcubes.builder.collection.SetBuilder;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public enum XPrimitive {

    BOOLEAN(boolean.class) {
        @Override
        public Object cast(Object value) {
            boolean b = (Boolean) value;
            return b;
        }
    },
    BYTE(byte.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).byteValue();
        }
    },
    CHARACTER(char.class) {
        @Override
        public Object cast(Object value) {
            return (char) toNumber(value).intValue();
        }
    },
    SHORT(short.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).shortValue();
        }
    },
    INTEGER(int.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).intValue();
        }
    },
    LONG(long.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).longValue();
        }
    },
    FLOAT(float.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).floatValue();
        }
    },
    DOUBLE(double.class) {
        @Override
        public Object cast(Object value) {
            return toNumber(value).doubleValue();
        }
    };

    public static final Set<XPrimitive> NUMBERS =
        SetBuilder.unmodifiableHashSet(BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE);

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

    private static Number toNumber(Object value) {
        if (value instanceof Character) {
            return (int) ((Character) value);
        }
        else {
            return (Number) value;
        }
    }

    public abstract Object cast(Object value);

}
