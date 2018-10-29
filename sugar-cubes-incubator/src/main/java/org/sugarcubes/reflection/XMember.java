package org.sugarcubes.reflection;

import java.lang.reflect.Member;

/**
 * Common part of {@link Member}'s wrappers.
 *
 * @author Maxim Butov
 */
public interface XMember<T extends Member> extends XReflectionObject<T> {

    default XClass<?> getDeclaringClass() {
        return new XClass<>(getReflectionObject().getDeclaringClass());
    }

}
