package org.sugarcubes.reflection;

import java.util.stream.Stream;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class XNullClass extends XClass<Object> {

    private static final class Null {

    }

    private XNullClass() {
        super((Class) Null.class);
    }

    public static XClass INSTANCE = new XNullClass();

    public static boolean isNull(XClass xClass) {
        return INSTANCE.equals(xClass);
    }

    @Override
    public XClass<?> getSuperClass() {
        return INSTANCE;
    }

    @Override
    public Stream<XClass<?>> getInterfaces() {
        return Stream.empty();
    }

    @Override
    public Stream<XClass<?>> getAllInterfaces() {
        return Stream.empty();
    }

    @Override
    public Class<Object> getJavaClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public XClassPackage getPackage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public Stream<XConstructor<Object>> getConstructors() {
        return Stream.empty();
    }

    @Override
    public Stream<XField> getFields() {
        return Stream.empty();
    }

    @Override
    public Stream<XField> getAllFields() {
        return Stream.empty();
    }

    @Override
    public Stream<XMethod<?>> getMethods() {
        return Stream.empty();
    }

    @Override
    public Stream<XMethod<?>> getAllMethods() {
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
