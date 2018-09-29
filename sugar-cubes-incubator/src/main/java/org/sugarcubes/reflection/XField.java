package org.sugarcubes.reflection;

import java.lang.reflect.Field;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XField<T> extends XReflectionObject<Field> implements XModifiers {

    public XField(Field reflectionObject) {
        super(reflectionObject);
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    @Override
    public int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    public T get(Object obj) {
        return XReflection.execute(() -> getReflectionObject().get(obj));
    }

    public void set(Object obj, T value) {
        XReflection.execute(() -> getReflectionObject().set(obj, value));
    }

}
