package org.sugarcubes.proxy;

import java.io.Serializable;

/**
 * @author Maxim Butov
 */
public class TypeDef implements Serializable {

    private final String name;

    private final TypeDef superclass;
    private final TypeDef[] interfaces;

    public TypeDef(Class clazz) {

        name = clazz.getName();

        Class superclazz = clazz.getSuperclass();
        superclass = superclazz != null ? new TypeDef(superclazz) : null;

        Class[] interfacez = clazz.getInterfaces();
        interfaces = new TypeDef[interfacez.length];
        for (int k = 0; k < interfacez.length; k++) {
            interfaces[k] = new TypeDef(interfacez[k]);
        }

    }

    public String getName() {
        return name;
    }

    public TypeDef getSuperclass() {
        return superclass;
    }

    public TypeDef[] getInterfaces() {
        return interfaces;
    }

    public Class toClass() {
        return toClass(null);
    }

    public Class toClass(ClassLoader classLoader) {
        try {
            return Class.forName(name, true, ClassLoaders.getClassLoader(classLoader));
        }
        catch (ClassNotFoundException e) {
            throw new ProxyError(e);
        }
    }

}
