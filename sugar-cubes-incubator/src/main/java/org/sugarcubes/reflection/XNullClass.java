package org.sugarcubes.reflection;

import java.util.stream.Stream;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class XNullClass extends XClass<Object> {

    private static final Class<Object> NULL = (Class) new Object() {
        // "null" class
    }.getClass();

    private XNullClass() {
        super(NULL);
    }

    public static XClass INSTANCE = new XNullClass();

    public static boolean isNull(XClass xClass) {
        return INSTANCE.equals(xClass);
    }

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public Class<Object> getReflectionObject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public XClassPackage getPackage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public XClass<?> getSuperclass() {
        return INSTANCE;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public Stream<XClass<?>> getDeclaredInterfaces() {
        return Stream.empty();
    }

    @Override
    public Stream<XClass<?>> getInterfaces() {
        return Stream.empty();
    }

    @Override
    public Stream<XConstructor<Object>> getDeclaredConstructors() {
        return Stream.empty();
    }

    @Override
    public Stream<XConstructor<Object>> getConstructors() {
        return Stream.empty();
    }

    @Override
    public Stream<XField<?>> getDeclaredFields() {
        return Stream.empty();
    }

    @Override
    public Stream<XField<?>> getFields() {
        return Stream.empty();
    }

    @Override
    public Stream<XMethod<?>> getDeclaredMethods() {
        return Stream.empty();
    }

    @Override
    public Stream<XMethod<?>> getMethods() {
        return Stream.empty();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof XNullClass;
    }

}
