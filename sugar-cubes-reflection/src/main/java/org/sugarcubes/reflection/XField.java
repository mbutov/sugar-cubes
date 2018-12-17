package org.sugarcubes.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.sugarcubes.reflection.XReflectionUtils.execute;

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
        super(reflectionObject);
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.modifiers = reflectionObject.getModifiers();
    }

    private static final XField<Integer> MODIFIERS = XReflection.of(Field.class).getField("modifiers");

    @Override
    protected Field loadReflectionObject() throws ReflectiveOperationException {
        Field field = declaringClass.getDeclaredField(name);
        if (field.getModifiers() != modifiers) {
            MODIFIERS.set(field, modifiers);
        }
        return field;
    }


    public F get(Object obj) {
        return execute(() -> getReflectionObject().get(obj));
    }

    public void set(Object obj, F value) {
        execute(() -> getReflectionObject().set(obj, value));
    }

    public void setModifiers(int modifiers) {
        if (this.modifiers != modifiers) {
            this.modifiers = modifiers;
            MODIFIERS.set(getReflectionObject(), modifiers);
        }
    }

    public void setModifier(int modifier, boolean newValue) {
        if (!XModifiers.isValidModifier(modifier)) {
            throw new IllegalArgumentException("Invalid modifier 0x" + Integer.toHexString(modifier));
        }
        int modifiers = this.modifiers;
        modifiers = newValue ? modifiers | modifier : modifiers & ~modifier;
        setModifiers(modifiers);
    }

    public void setFinal(boolean isFinal) {
        setModifier(Modifier.FINAL, isFinal);
    }

    public void clearFinal() {
        setFinal(false);
    }

}
