package org.sugarcubes.reflection;

import java.lang.reflect.Member;

/**
 * Common part of {@link Member}'s wrappers.
 *
 * @author Maxim Butov
 */
public interface XMember<T extends Member> extends XModifiers, XReflectionObject<T> {

    @Override
    default String getName() {
        return getReflectionObject().getName();
    }

    @Override
    default int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    default XClass<?> getDeclaringClass() {
        return XReflection.of(getReflectionObject().getDeclaringClass());
    }

}
