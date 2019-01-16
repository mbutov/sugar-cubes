package org.sugarcubes.proxy;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Maxim Butov
 */
public class ClassLoaders {

    public static ClassLoader getClassLoader() {
        return getClassLoader(null, null);
    }

    public static ClassLoader getClassLoader(Class clazz) {
        return getClassLoader(null, clazz);
    }

    public static ClassLoader getClassLoader(ClassLoader classLoader) {
        return getClassLoader(classLoader, null);
    }

    public static ClassLoader getClassLoader(ClassLoader classLoader, Class clazz) {
        return Stream.of(
            classLoader,
            getContextClassLoader(),
            clazz != null ? clazz.getClassLoader() : null,
            ClassLoaders.class.getClassLoader(),
            ClassLoader.getSystemClassLoader()
        )
            .filter(Objects::nonNull)
            .findFirst()
            .get();
    }

    public static ClassLoader getContextClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        }
        catch (Exception e) {
            return null;
        }
    }

}
