package org.sugarcubes.reflection;

/**
 * Base interface for reflection objects wrappers.
 *
 * @author Maxim Butov
 */
public interface XReflectionObject<T> {

    T getReflectionObject();

    String getName();

}
