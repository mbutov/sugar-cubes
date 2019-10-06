package org.sugarcubes.reflection;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

/**
 * Modifiers-related methods.
 *
 * @author Maxim Butov
 */
public interface XModifiers {

    /**
     * Checks that integer has single bit set.
     *
     * @param modifier value to check
     *
     * @return result
     */
    static boolean isValidModifier(int modifier) {
        return (modifier != 0) && ((modifier & (modifier - 1)) == 0);
    }

    /**
     * @return set of modifiers of reflection object
     */
    int getModifiers();

    default boolean isModifier(int modifier) {
        return (getModifiers() & modifier) != 0;
    }

    default boolean isModifier(IntPredicate method) {
        return method.test(getModifiers());
    }

    default Set<XModifier> getXModifiers() {
        return Arrays.stream(XModifier.values())
            .filter(this::isXModifier)
            .collect(Collectors.toSet());
    }

    default boolean isXModifier(XModifier modifier) {
        return (getModifiers() & modifier.getIntValue()) != 0;
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
