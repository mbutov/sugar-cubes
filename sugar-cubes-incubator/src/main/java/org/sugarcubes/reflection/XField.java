package org.sugarcubes.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.sugarcubes.reflection.XReflectionUtils.execute;
import static org.sugarcubes.reflection.XReflectionUtils.tryToMakeAccessible;

/**
 * Wrapper for {@link Field}.
 *
 * @author Maxim Butov
 */
public class XField<F> extends XReloadableReflectionObject<Field>
    implements XAnnotated<Field>, XMember<Field>, XModifiers {

    private final Class declaringClass;
    private final String name;

    private int modifiers;

    XField(Field reflectionObject) {
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.modifiers = reflectionObject.getModifiers();
    }

    @Override
    protected Field loadReflectionObject() {
        Field field = execute(() -> tryToMakeAccessible(declaringClass.getDeclaredField(name)));
        if (getModifiers() != modifiers) {
            setModifiers(modifiers);
        }
        return field;
    }

    public F get(Object obj) {
        return execute(() -> getReflectionObject().get(obj));
    }

    public void set(Object obj, F value) {
        execute(() -> getReflectionObject().set(obj, value));
    }

    private static final XField<Integer> MODIFIERS = XReflection.of(Field.class).getField("modifiers");

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
        MODIFIERS.set(getReflectionObject(), modifiers);
    }

    public void setModifier(int modifier, boolean newValue) {
        if (modifier == 0 || (modifier & (modifier - 1)) != 0) {
            throw new IllegalArgumentException("Invalid modifier 0x" + Integer.toHexString(modifier));
        }
        int modifiers = MODIFIERS.get(getReflectionObject());
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
