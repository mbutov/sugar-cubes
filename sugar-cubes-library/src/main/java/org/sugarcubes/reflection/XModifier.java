package org.sugarcubes.reflection;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * @author Maxim Butov
 */
public enum XModifier {

    PUBLIC(0x00000001),
    PRIVATE(0x00000002),
    PROTECTED(0x00000004),
    STATIC(0x00000008),
    FINAL(0x00000010),
    SYNCHRONIZED(0x00000020),
    VOLATILE(0x00000040),
    TRANSIENT(0x00000080),
    NATIVE(0x00000100),
    INTERFACE(0x00000200),
    ABSTRACT(0x00000400),
    STRICT(0x00000800),

    BRIDGE(0x00000040),
    VARARGS(0x00000080),
    SYNTHETIC(0x00001000),
    ANNOTATION(0x00002000),
    ENUM(0x00004000),
    MANDATED(0x00008000),

    ;

    private static Set<XModifier> set(XModifier first, XModifier... rest) {
        return Collections.unmodifiableSet(EnumSet.of(first, rest));
    }

    public static final Set<XModifier> CLASS_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE, ABSTRACT, STATIC, FINAL, STRICT);
    public static final Set<XModifier> INTERFACE_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE, ABSTRACT, STATIC, STRICT);
    public static final Set<XModifier> CONSTRUCTOR_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE);
    public static final Set<XModifier> METHOD_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE, ABSTRACT, STATIC, FINAL, SYNCHRONIZED,
        NATIVE, STRICT);
    public static final Set<XModifier> FIELD_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE, STATIC, FINAL, TRANSIENT, VOLATILE);
    public static final Set<XModifier> PARAMETER_MODIFIERS = set(FINAL);
    public static final Set<XModifier> ACCESS_MODIFIERS = set(PUBLIC, PROTECTED, PRIVATE);

    private final int intValue;

    XModifier(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }

}
