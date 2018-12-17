package org.sugarcubes.reflection;

import java.lang.reflect.Modifier;
import java.util.function.IntPredicate;

/**
 * Modifiers-related methods.
 *
 * @author Maxim Butov
 */
public interface XModifiers {

    int getModifiers();

    default boolean isModifier(int modifier) {
        return (getModifiers() & modifier) != 0;
    }

    default boolean isModifier(IntPredicate method) {
        return method.test(getModifiers());
    }

    default boolean isPublic() {
        return isModifier(Modifier::isPublic);
    }

    default boolean isPrivate() {
        return isModifier(Modifier::isPrivate);
    }

    default boolean isProtected() {
        return isModifier(Modifier::isProtected);
    }

    default boolean isPackage() {
        return !isPublic() && !isPrivate() && !isProtected();
    }

    default boolean isStatic() {
        return isModifier(Modifier::isStatic);
    }

    default boolean isFinal() {
        return isModifier(Modifier::isFinal);
    }

    default boolean isSynchronized() {
        return isModifier(Modifier::isSynchronized);
    }

    default boolean isVolatile() {
        return isModifier(Modifier::isVolatile);
    }

    default boolean isTransient() {
        return isModifier(Modifier::isTransient);
    }

    default boolean isNative() {
        return isModifier(Modifier::isNative);
    }

    default boolean isInterface() {
        return isModifier(Modifier::isInterface);
    }

    default boolean isAbstract() {
        return isModifier(Modifier::isAbstract);
    }

    default boolean isStrict() {
        return isModifier(Modifier::isStrict);
    }

}
