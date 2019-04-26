package org.sugarcubes.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.sugarcubes.arg.Arg;
import static org.sugarcubes.reflection.XReflectionUtils.execute;

/**
 * Wrapper for {@link Field}.
 *
 * @author Maxim Butov
 */
public class XField<T> extends XReloadableReflectionObject<Field>
    implements XAnnotated<Field>, XMember<Field>, XModifiers {

    /**
     * {@link Field#getDeclaringClass()}
     */
    private final Class declaringClass;

    /**
     * {@link Field#getName()}
     */
    private final String name;

    /**
     * {@link Field#getModifiers()}
     */
    private final int modifiers;

    XField(Field reflectionObject) {
        super(reflectionObject);
        this.declaringClass = reflectionObject.getDeclaringClass();
        this.name = reflectionObject.getName();
        this.modifiers = reflectionObject.getModifiers();
    }

    private Field loadField() throws ReflectiveOperationException {
        return declaringClass.getDeclaredField(name);
    }

    @Override
    protected Field loadReflectionObject() throws ReflectiveOperationException {
        return fieldWithModifiers(loadField(), modifiers);
    }

    public T get(Object obj) {
        return execute(() -> getReflectionObject().get(obj));
    }

    public void set(Object obj, T value) {
        execute(() -> getReflectionObject().set(obj, value));
    }

    public T put(Object obj, T value) {
        T oldValue = get(obj);
        set(obj, value);
        return oldValue;
    }

    public T copy(Object from, Object to) {
        T value = get(from);
        set(to, value);
        return value;
    }

    public XFieldAccessor<T> getAccessor(Object obj) {
        return new XFieldAccessorImpl<>(this, obj);
    }

    public <X> XField<X> cast() {
        return (XField) this;
    }

    public XField<T> withModifiers(int modifiers) {
        return this.modifiers != modifiers ?
            new XField<>(fieldWithModifiers(execute(this::loadField), modifiers)) : this;
    }

    public XField<T> withModifier(int modifier, boolean newValue) {
        Arg.check(modifier, XModifiers::isValidModifier, () -> "Invalid modifier 0x" + Integer.toHexString(modifier));
        int newModifiers = newValue ? modifiers | modifier : modifiers & ~modifier;
        return withModifiers(newModifiers);
    }

    public XField<T> withFinal(boolean isFinal) {
        return withModifier(Modifier.FINAL, isFinal);
    }

    public XField<T> withNoFinal() {
        return withFinal(false);
    }

    /// private stuff

    private static final XField<Integer> MODIFIERS = XReflection.of(Field.class).getDeclaredField("modifiers");

    private static Field fieldWithModifiers(Field field, int modifiers) {
        if (field.getModifiers() != modifiers) {
            MODIFIERS.set(field, modifiers);
        }
        return field;
    }

}
