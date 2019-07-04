package org.sugarcubes.primitive;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Primitive type descriptor which encapsulates information about primitive type, primitive array type,
 * wrapper type, and can instantiate arrays, access elements by index, write/read values into/from stream.
 *
 * @author Maxim Butov
 */
public abstract class XPrimitive<W, A> {

    /**
     * Primitive type.
     */
    private final Class<W> primitiveType;

    /**
     * Array type.
     */
    private final Class<A> arrayType;

    /**
     * Wrapper type.
     */
    private final Class<W> wrapperType;

    /**
     * Default value.
     */
    private final W defaultValue;

    XPrimitive() {

        A array = newArray(1);

        arrayType = (Class) array.getClass();
        primitiveType = (Class) arrayType.getComponentType();
        defaultValue = get(array, 0);
        wrapperType = (Class) defaultValue.getClass();

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

    public abstract A newArray(int length);

    public abstract int length(A array);

    public abstract W get(A array, int index);

    public abstract void set(A array, int index, W value);

    public abstract void write(DataOutputStream out, W value) throws IOException;

    public abstract W read(DataInputStream in) throws IOException;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XPrimitive)) {
            return false;
        }
        return arrayType.equals(((XPrimitive) obj).arrayType);
    }

    @Override
    public int hashCode() {
        return arrayType.hashCode();
    }

    @Override
    public String toString() {
        return String.format("XPrimitive<%s,%s>", wrapperType.getSimpleName(), arrayType.getSimpleName());
    }

}
