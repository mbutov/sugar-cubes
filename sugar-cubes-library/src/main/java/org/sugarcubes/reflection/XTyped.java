package org.sugarcubes.reflection;

/**
 * "Type" mixin for reflection wrappers.
 *
 * Is either class (for class and constructor wrappers) or method return type or field type.
 *
 * @author Maxim Butov
 */
public interface XTyped<T> {

    Class<T> getType();

    default boolean isType(Class<?> type) {
        return type.isAssignableFrom(getType());
    }

}
