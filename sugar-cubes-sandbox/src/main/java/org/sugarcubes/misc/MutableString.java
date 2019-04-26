package org.sugarcubes.misc;

import java.util.Arrays;

import org.sugarcubes.arg.Arg;
import org.sugarcubes.reflection.XField;
import org.sugarcubes.reflection.XReflection;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class MutableString implements CharSequence {

    private final String value;

    public MutableString(String value) {
        Arg.notNull(value, "value is null");
        this.value = value;
    }

    private static final char[] EMPTY_CHAR_ARRAY = {};

    private static final XField<char[]> STRING_VALUE_FIELD = XReflection.of(String.class).<char[]>getDeclaredField("value")
        .withNoFinal();
    private static final XField<Integer> STRING_HASH_FIELD = XReflection.of(String.class).getDeclaredField("hash");

    public void clear() {
        Arrays.fill(STRING_VALUE_FIELD.get(value), (char) 0);
        STRING_VALUE_FIELD.set(value, EMPTY_CHAR_ARRAY);
        STRING_HASH_FIELD.set(value, 0);
    }

    public void setValue(String newValue) {
        clear();
        STRING_VALUE_FIELD.copy(newValue, value);
        STRING_HASH_FIELD.copy(newValue, value);
    }

    public void setValue(char[] newValue) {
        clear();
        STRING_VALUE_FIELD.set(value, newValue);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

}
