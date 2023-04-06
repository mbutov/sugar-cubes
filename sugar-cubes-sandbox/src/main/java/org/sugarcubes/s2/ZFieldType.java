package org.sugarcubes.s2;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * <p>
 * Enum class that describes the type of a field encoded inside a classdesc description.
 * </p>
 *
 * <p>
 * This stores both information on the type (reference/array vs. primitive) and, in cases
 * of reference or array types, the name of the class being referred to.
 * </p>
 */
public enum ZFieldType {

    BYTE('B') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setByte(obj, in.readByte());
        }
    },
    CHAR('C') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setChar(obj, in.readChar());
        }
    },
    DOUBLE('D') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setDouble(obj, in.readDouble());
        }
    },
    FLOAT('F') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setFloat(obj, in.readFloat());
        }
    },
    INTEGER('I') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setInt(obj, in.readInt());
        }
    },
    LONG('J') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setLong(obj, in.readLong());
        }
    },
    SHORT('S') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setShort(obj, in.readShort());
        }
    },
    BOOLEAN('Z') {
        @Override
        public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
            field.setBoolean(obj, in.readBoolean());
        }
    },
    ARRAY('[') {
        @Override
        public boolean isPrimitive() {
            return false;
        }
    },
    OBJECT('L') {
        @Override
        public boolean isPrimitive() {
            return false;
        }
    };

    private final byte code;

    public boolean isPrimitive() {
        return true;
    }

    public void read(Object obj, Field field, DataInput in) throws IOException, IllegalAccessException {
        throw new UnsupportedOperationException();
    }

    /**
     * Constructor.
     *
     * @param code the character representing the type (must match one of those listed in
     * prim_typecode or obj_typecode in the Object Serialization Stream Protocol)
     */
    ZFieldType(int code) {
        this.code = (byte) code;
    }

    /**
     * Gets the type character for this field.
     *
     * @return the type code character for this field; values will be one of those in
     * prim_typecode or obj_typecode in the protocol spec
     */
    public byte code() {
        return code;
    }

    /**
     * Given a byte containing a type code, return the corresponding enum.
     *
     * @param b the type code; must be one of the charcaters in obj_typecode or
     * prim_typecode in the protocol spec
     * @return the corresponding fieldtype enum
     * @throws IOException if the type code is invalid
     */
    public static ZFieldType get(byte b) throws IOException {
        switch (b) {
            case 'B':
                return BYTE;
            case 'C':
                return CHAR;
            case 'D':
                return DOUBLE;
            case 'F':
                return FLOAT;
            case 'I':
                return INTEGER;
            case 'J':
                return LONG;
            case 'S':
                return SHORT;
            case 'Z':
                return BOOLEAN;
            case '[':
                return ARRAY;
            case 'L':
                return OBJECT;
            default:
                throw new IOException(String.format("Invalid field type: %s.", (char) b));
        }
    }
}
