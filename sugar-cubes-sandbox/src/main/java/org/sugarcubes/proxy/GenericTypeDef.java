package org.sugarcubes.proxy;

import java.io.Serializable;

/**
 * @author Maxim Butov
 */
public class GenericTypeDef implements Serializable {

    private final String type;
    private final String[] interfaces;

    public GenericTypeDef(String type, String[] interfaces) {
        this.type = type;
        this.interfaces = interfaces;
    }

    public String getType() {
        return type;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public static GenericTypeDef of(GenericType type) {
        Class[] interfaces = type.getInterfaces();
        String[] interfaceNames = new String[interfaces.length];
        for (int k = 0; k < interfaces.length; k++) {
            interfaceNames[k] = interfaces[k].getName();
        }
        return new GenericTypeDef(type.getType().getName(), interfaceNames);
    }

    public GenericType toGenericType() {
        return toGenericType(null);
    }

    public GenericType toGenericType(ClassLoader classLoader) {
        Class baseClass = loadClass(classLoader, this.type);
        Class[] interfaceClasses = new Class[this.interfaces.length];
        for (int k = 0; k < interfaces.length; k++) {
            interfaceClasses[k] = loadClass(classLoader, interfaces[k]);
        }
        return GenericType.create(baseClass, interfaceClasses);
    }

    private static Class loadClass(ClassLoader classLoader, String name) {
        try {
            return Class.forName(name, true, ClassLoaders.getClassLoader(classLoader));
        }
        catch (ClassNotFoundException e) {
            throw new ProxyError(e);
        }
    }
}
