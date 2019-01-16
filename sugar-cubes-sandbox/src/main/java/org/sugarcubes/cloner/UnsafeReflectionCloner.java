package org.sugarcubes.cloner;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

import org.sugarcubes.builder.collection.MapBuilder;
import org.sugarcubes.function.TernaryConsumer;
import org.sugarcubes.reflection.XField;

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

    private static <X> UnsafeCopyOperation getCopyOperation(TernaryConsumer<Object, Long, X> put, BiFunction<Object, Long, X> get) {
        return (from, offset, into) -> put.accept(into, offset, get.apply(from, offset));
    }

    /**
     * Mapping of primitive types to copy operations.
     */
    private static final Map<Class, UnsafeCopyOperation> OPERATIONS = MapBuilder.<Class, UnsafeCopyOperation>hashMap()
        .put(boolean.class, getCopyOperation(UNSAFE::putBoolean, UNSAFE::getBoolean))
        .put(byte.class, getCopyOperation(UNSAFE::putByte, UNSAFE::getByte))
        .put(char.class, getCopyOperation(UNSAFE::putChar, UNSAFE::getChar))
        .put(short.class, getCopyOperation(UNSAFE::putShort, UNSAFE::getShort))
        .put(int.class, getCopyOperation(UNSAFE::putInt, UNSAFE::getInt))
        .put(long.class, getCopyOperation(UNSAFE::putLong, UNSAFE::getLong))
        .put(float.class, getCopyOperation(UNSAFE::putFloat, UNSAFE::getFloat))
        .put(double.class, getCopyOperation(UNSAFE::putDouble, UNSAFE::getDouble))
        .transform(Collections::unmodifiableMap)
        .build();

    @Override
    protected void copyField(Object from, Object into, XField<Object> field, Map<Object, Object> cloned) {
        long offset = UNSAFE.objectFieldOffset(field.getReflectionObject());
        UnsafeCopyOperation operation = OPERATIONS.get(field.getReflectionObject().getType());
        if (operation != null) {
            operation.copy(from, offset, into);
        }
        else {
            UNSAFE.putObject(into, offset, doClone(UNSAFE.getObject(from, offset), cloned));
        }
    }

}
