package org.sugarcubes.cloner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

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
    @FunctionalInterface
    private interface UnsafeCopyOperation {

        /**
         * Copy field with specified {@code offset} from {@code from} into {@code into}.
         */
        void copy(Object from, long offset, Object into);

    }

    /**
     * 3-arguments consumer.
     */
    @FunctionalInterface
    private interface ThreeConsumer<T, U, V> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         * @param v the third input argument
         */
        void accept(T t, U u, V v);

    }

    private static <X> UnsafeCopyOperation getCopyOperation(ThreeConsumer<Object, Long, X> put, BiFunction<Object, Long, X> get) {
        return (from, offset, into) -> put.accept(into, offset, get.apply(from, offset));
    }

    /**
     * Mapping of primitive types to copy operations.
     */
    private static final Map<Class, UnsafeCopyOperation> OPERATIONS;

    static {
        Map<Class, UnsafeCopyOperation> operations = new HashMap<>();
        operations.put(boolean.class, getCopyOperation(UNSAFE::putBoolean, UNSAFE::getBoolean));
        operations.put(byte.class, getCopyOperation(UNSAFE::putByte, UNSAFE::getByte));
        operations.put(char.class, getCopyOperation(UNSAFE::putChar, UNSAFE::getChar));
        operations.put(short.class, getCopyOperation(UNSAFE::putShort, UNSAFE::getShort));
        operations.put(int.class, getCopyOperation(UNSAFE::putInt, UNSAFE::getInt));
        operations.put(long.class, getCopyOperation(UNSAFE::putLong, UNSAFE::getLong));
        operations.put(float.class, getCopyOperation(UNSAFE::putFloat, UNSAFE::getFloat));
        operations.put(double.class, getCopyOperation(UNSAFE::putDouble, UNSAFE::getDouble));
        OPERATIONS = Collections.unmodifiableMap(operations);
    }

    @Override
    protected void setWritable(Field field) {
        // Unsafe doesn't need this
    }

    @Override
    protected void copyField(Object from, Object into, Field field, Map<Object, Object> cloned) {
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
