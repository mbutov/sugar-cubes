package org.sugarcubes.reflection;

import java.lang.reflect.Field;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XField<T> {

    private final Field javaField;

    public XField(Field javaField) {
        this.javaField = XReflection.prepare(javaField);
    }

    public Field getJavaField() {
        return javaField;
    }

    public T get(Object obj) {
        return XReflection.execute(() -> javaField.get(obj));
    }

    public void set(Object obj, T value) {
        XReflection.execute(() -> javaField.set(obj, value));
    }

    public T getStatic() {
        return get(null);
    }

    public void setStatic(T value) {
        set(null, value);
    }

}
