package org.sugarcubes.proxy;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representation of an object type. Can be Java type or proxy type.
 *
 * @author Maxim Butov
 */
public final class GenericType<T> implements Type, Serializable {

    public static final GenericType<Object> OBJECT = create().unmodifiable();
    public static final GenericType<Void> VOID = create(Void.class).unmodifiable();

    private final Class type;
    private final Set<Class> interfaces;

    private GenericType(Class type, Set<Class> interfaces) {
        this.type = type;
        this.interfaces = interfaces;
    }

    private GenericType(Class<T> type) {
        this(type, new HashSet<Class>());
    }

    public static GenericType<Object> create() {
        return new GenericType<Object>(Object.class);
    }

    public static <T> GenericType<T> create(Class<T> classOrInterface, Class... interfaces) {
        GenericType type;
        if (classOrInterface.isInterface()) {
            type = create().implementing(classOrInterface);
        }
        else {
            if (ProxyUtils.isProxyClass(classOrInterface)) {
                type = new GenericType<T>((Class) classOrInterface.getSuperclass())
                    .implementing(classOrInterface.getInterfaces());
            }
            else {
                type = new GenericType<T>(classOrInterface);
            }
        }
        return type.implementing(interfaces);
    }

    public boolean isInterfacesOnly() {
        return Object.class.equals(type) && !isJavaClass();
    }

    public boolean isJavaClass() {
        return interfaces.isEmpty();
    }

    public Class getJavaClass() {
        if (!isJavaClass()) {
            throw new IllegalStateException(this + " does not represent java class.");
        }
        return type;
    }

    public <V> GenericType<V> implementing(Class... interfaces) {
        if (interfaces != null) {
            for (Class i : interfaces) {
                if (!i.isInterface()) {
                    throw new IllegalArgumentException(i + " is not an interface.");
                }
                addInterface(i);
            }
        }
        return cast();
    }

    public <V> GenericType<V> cast() {
        return (GenericType<V>) this;
    }

    public <V> GenericType<V> extendable() {
        // todo
        return cast();
    }

    @SuppressWarnings("unchecked")
    private void addInterface(Class newInterface) {
        if (newInterface.isAssignableFrom(type)) {
            return;
        }
        Set<Class> toRemove = new HashSet<Class>();
        for (Class theInterface : interfaces) {
            if (newInterface.isAssignableFrom(theInterface)) {
                return;
            }
            if (theInterface.isAssignableFrom(newInterface)) {
                toRemove.add(theInterface);
            }
        }
        if (!canBeExtended()) {
            throw new UnsupportedOperationException("Type " + this + " cannot be extended.");
        }
        interfaces.removeAll(toRemove);
        interfaces.add(newInterface);
    }

    public boolean canBeExtended() {
        return !Modifier.isFinal(type.getModifiers());
    }

    public Class getType() {
        return type;
    }

    public Class[] getInterfaces() {
        return interfaces.toArray(new Class[interfaces.size()]);
    }

    public Class[] getAllInterfaces() {
        Set<Class> allInterfaces = new HashSet<Class>();
        for (Class c = type; c != null; c = c.getSuperclass()) {
            addAllInterfaces(allInterfaces, c.getInterfaces());
        }
        addAllInterfaces(allInterfaces, getInterfaces());
        return allInterfaces.toArray(new Class[allInterfaces.size()]);
    }

    private static void addAllInterfaces(Set<Class> allInterfaces, Class... interfaces) {
        for (Class i : interfaces) {
            allInterfaces.add(i);
            addAllInterfaces(allInterfaces, i.getInterfaces());
        }
    }

    public GenericType<T> copy() {
        return new GenericType<T>(type, new HashSet<Class>(interfaces));
    }

    public GenericType<T> unmodifiable() {
        return new GenericType<T>(type, Collections.unmodifiableSet(new HashSet<Class>(interfaces)));
    }

    @SuppressWarnings("unchecked")
    public boolean isAssignableFrom(GenericType another) {
        if (!another.isOfType(type)) {
            return false;
        }
        for (Class i : interfaces) {
            if (!another.isOfType(i)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean isOfType(Class clazz) {
        if (clazz.isAssignableFrom(type)) {
            return true;
        }
        if (clazz.isInterface()) {
            for (Class i : interfaces) {
                if (clazz.isAssignableFrom(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAssignableFrom(Class clazz) {
        return isAssignableFrom(create(clazz));
    }

    public boolean isInstance(Object obj) {
        return obj != null && isAssignableFrom(obj.getClass());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GenericType)) {
            return false;
        }

        GenericType that = (GenericType) obj;

        return type.equals(that.type) && interfaces.equals(that.interfaces);
    }

    @Override
    public int hashCode() {
        return Arrays.asList(type, interfaces).hashCode();
    }

    @Override
    public String toString() {
        return "GenericType{" +
            "type=" + type +
            ", interfaces=" + interfaces +
            '}';
    }

}
