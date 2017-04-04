package org.sugarcubes.concurrent;

import java.io.IOException;

/**
 * @author Maxim Butov
 */
public class Methods {

    static void voidNoException() {
    }

    static void voidCheckedException() throws IOException {
    }

    static void voidException() throws Exception {
    }

    static Object objectNoException() {
        return null;
    }

    static Object objectCheckedException() throws IOException {
        return null;
    }

    static Object objectException() throws Exception {
        return null;
    }

}
