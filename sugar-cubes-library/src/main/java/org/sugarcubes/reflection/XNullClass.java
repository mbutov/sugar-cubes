package org.sugarcubes.reflection;

import java.util.stream.Stream;
import static java.util.stream.Stream.empty;

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
        return empty();
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
        return empty();
    }

    @Override
    public Stream<XClass<?>> getInterfaces() {
        return empty();
    }

    @Override
    public Stream<XConstructor<Object>> getConstructors() {
        return empty();
    }

    @Override
    public Stream<XField<?>> getDeclaredFields() {
        return empty();
    }

    @Override
    public Stream<XField<?>> getFields() {
        return empty();
    }

    @Override
    public Stream<XMethod<?>> getDeclaredMethods() {
        return empty();
    }

    @Override
    public Stream<XMethod<?>> getMethods() {
        return empty();
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

}
