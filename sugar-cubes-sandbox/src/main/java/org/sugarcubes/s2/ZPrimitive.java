package org.sugarcubes.s2;

import java.io.DataInput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ZPrimitive {

    BOOLEAN(boolean.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            boolean[] array = new boolean[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readBoolean();
            }
            return array;
        }
    },
    BYTE(byte.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            byte[] array = new byte[length];
            in.readFully(array);
            return array;
        }
    },
    CHARACTER(char.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            char[] array = new char[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readChar();
            }
            return array;
        }
    },
    SHORT(short.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            short[] array = new short[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readShort();
            }
            return array;
        }
    },
    INTEGER(int.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            int[] array = new int[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readInt();
            }
            return array;
        }
    },
    LONG(long.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            long[] array = new long[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readLong();
            }
            return array;
        }
    },
    FLOAT(float.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            float[] array = new float[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readFloat();
            }
            return array;
        }
    },
    DOUBLE(double.class) {
        @Override
        public Object readArray(int length, DataInput in) throws IOException {
            double[] array = new double[length];
            for (int k = 0; k < length; k++) {
                array[k] = in.readDouble();
            }
            return array;
        }
    };

    private final Class<?> type;

    ZPrimitive(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }

    public abstract Object readArray(int length, DataInput in) throws IOException;

    private static final Map<Class<?>, ZPrimitive> MAP =
        Arrays.stream(values()).collect(Collectors.toMap(ZPrimitive::getType, Function.identity()));

    public static ZPrimitive valueOf(Class<?> type) {
        return MAP.computeIfAbsent(type, key -> {
            throw new IllegalArgumentException(String.format("Non-primitive type %s.", type));
        });
    }

}
