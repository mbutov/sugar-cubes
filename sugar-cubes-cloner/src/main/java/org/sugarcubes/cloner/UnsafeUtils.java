package org.sugarcubes.cloner;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * Utility class to obtain {@link Unsafe} instance.
 *
 * @author Maxim Butov
 */
public class UnsafeUtils {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return {@link Unsafe} instance
     */
    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

}
