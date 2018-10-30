package org.sugarcubes.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Wrapper for {@link Field}.
 *
 * @author Maxim Butov
 */
public class XField<T> extends XReflectionObjectImpl<Field>
    implements XAnnotated<Field>, XMember<Field>, XModifiers {

    XField(Field reflectionObject) {
        super(reflectionObject);
        XReflectionUtils.tryToMakeAccessible(reflectionObject);
    }

    public T get(Object obj) {
        return XReflectionUtils.execute(() -> getReflectionObject().get(obj));
    }

    public void set(Object obj, T value) {
        XReflectionUtils.execute(() -> getReflectionObject().set(obj, value));
    }

    private static final XField<Integer> MODIFIERS = XReflection.of(Field.class).getField("modifiers");

    public void setModifiers(int modifiers) {
        MODIFIERS.set(getReflectionObject(), modifiers);
    }

    public void setModifier(int modifier, boolean newValue) {
        if (modifier == 0 || (modifier & (modifier - 1)) != 0) {
            throw new IllegalArgumentException("Invalid modifier 0x" + Integer.toHexString(modifier));
        }
        int modifiers = getModifiers();
        boolean oldValue = (modifiers & modifier) != 0;
        if (oldValue != newValue) {
            if (newValue) {
                modifiers += modifier;
            }
            else {
                modifiers -= modifier;
            }
            setModifiers(modifiers);
        }
    }

    public void setFinal(boolean isFinal) {
        setModifier(Modifier.FINAL, isFinal);
    }

    public void clearFinal() {
        setFinal(false);
    }

}
