package org.sugarcubes.reflection;

import java.io.ObjectStreamException;
import java.util.stream.Stream;

/**
 * A special kind of {@link XClass} meaning "no class".
 *
 * The objective of this class is do not return {@code null} in {@link XClass#getSuperclass()} method,
 * which simplifies expressions.
 *
 * @author Maxim Butov
 */
public final class XNullClass extends XClass<Object> {

    private static final Class<Object> NULL = (Class) new Object() {
        // special "null" class
    }.getClass();

    private XNullClass() {
        super(NULL);
    }

    public static XClass<Object> INSTANCE = new XNullClass();

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public Stream<XClass<?>> getInheritance() {
        return Stream.empty();
    }

    @Override
    public String getName() {
        return "null";
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

    @Override
    public String toString() {
        return "XNullClass";
    }

    private Object readResolve() throws ObjectStreamException {
        return INSTANCE;
    }

}
