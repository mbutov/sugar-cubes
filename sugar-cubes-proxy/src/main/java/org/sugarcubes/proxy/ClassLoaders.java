package org.sugarcubes.proxy;

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
        ClassLoader cl = classLoader;
        if (cl == null) {
            cl = getContextClassLoader();
            if (cl == null) {
                if (clazz != null) {
                    cl = clazz.getClassLoader();
                }
                if (cl == null) {
                    cl = ClassLoaders.class.getClassLoader();
                    if (cl == null) {
                        cl = ClassLoader.getSystemClassLoader();
                    }
                }
            }
        }
        return cl;
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
