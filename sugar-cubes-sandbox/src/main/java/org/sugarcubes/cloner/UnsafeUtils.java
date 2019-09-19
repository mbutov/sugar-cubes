package org.sugarcubes.cloner;

import org.sugarcubes.reflection.XReflection;

import sun.misc.Unsafe;

/**
 * Utility class to obtain {@link Unsafe} instance.
 *
 * @author Maxim Butov
 */
public class UnsafeUtils {

    private static final Unsafe UNSAFE = XReflection.of(Unsafe.class).<Unsafe>getDeclaredField("theUnsafe").staticGet();

    /**
     * @return {@link Unsafe} instance
     */
    public static Unsafe getUnsafe() {
        return UNSAFE;
    }

}
