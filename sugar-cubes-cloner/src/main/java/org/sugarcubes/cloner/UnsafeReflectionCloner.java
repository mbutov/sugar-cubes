package org.sugarcubes.cloner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sun.misc.Unsafe;

/**
 * Cloner implementation which uses {@link Unsafe} to create abjects and copy fields.
 *
 * @author Maxim Butov
 */
public class UnsafeReflectionCloner extends ReflectionCloner {

    /**
     * Singleton instance of the cloner.
     */
    public static final Cloner INSTANCE = new UnsafeReflectionCloner();

    /**
     * Default constructor.
     */
    public UnsafeReflectionCloner() {
        super(new UnsafeObjectFactory());
    }

    /**
     * {@link Unsafe} instance.
     */
    private static final Unsafe UNSAFE = UnsafeUtils.getUnsafe();

    /**
     * Functional interface for the field copy operation.
     */
    private interface UnsafeCopyOperation {

        /**
         * Copy field with specified {@code offset} from {@code from} into {@code into}.
         */
        void copy(Object from, long offset, Object into);

    }

    /**
     * Mapping of primitive types to copy operations.
     */
    private static final Map<Class, UnsafeCopyOperation> OPERATIONS;

    static {
        Map<Class, UnsafeCopyOperation> operations = new HashMap<>();
        operations.put(boolean.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putBoolean(into, offset, UNSAFE.getBoolean(from, offset));
            }
        });
        operations.put(byte.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putByte(into, offset, UNSAFE.getByte(from, offset));
            }
        });
        operations.put(char.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putChar(into, offset, UNSAFE.getChar(from, offset));
            }
        });
        operations.put(short.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putShort(into, offset, UNSAFE.getShort(from, offset));
            }
        });
        operations.put(int.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putInt(into, offset, UNSAFE.getInt(from, offset));
            }
        });
        operations.put(long.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putLong(into, offset, UNSAFE.getLong(from, offset));
            }
        });
        operations.put(float.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putFloat(into, offset, UNSAFE.getFloat(from, offset));
            }
        });
        operations.put(double.class, new UnsafeCopyOperation() {
            @Override
            public void copy(Object from, long offset, Object into) {
                UNSAFE.putDouble(into, offset, UNSAFE.getDouble(from, offset));
            }
        });
        OPERATIONS = Collections.unmodifiableMap(operations);
    }

    @Override
    protected void setWritable(Field field, int modifiers) {
        // Unsafe doesn't need this
    }

    @Override
    protected void copyField(Object from, Object into, Field field, Map cloned) {
        long offset = UNSAFE.objectFieldOffset(field);
        UnsafeCopyOperation operation = OPERATIONS.get(field.getType());
        if (operation != null) {
            operation.copy(from, offset, into);
        }
        else {
            UNSAFE.putObject(into, offset, doClone(UNSAFE.getObject(from, offset), cloned));
        }
    }

}
