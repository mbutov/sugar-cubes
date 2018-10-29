package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XReflection {

    public static <X> XClass<X> of(Class<X> clazz) {
        return new XClass<>(clazz);
    }

    public static <X> XConstructor<X> of(Constructor<X> constructor) {
        return new XConstructor<>(constructor);
    }

    public static <X> XField<X> of(Field field) {
        return new XField<>(field);
    }

    public static <X> XMethod<X> of(Method method) {
        return new XMethod<>(method);
    }

}
