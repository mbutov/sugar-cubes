package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XConstructor<T> extends XExecutable<T> {

    private final Constructor<T> javaConstructor;

    public XConstructor(Constructor<T> javaConstructor) {
        this.javaConstructor = XReflection.prepare(javaConstructor);
    }

    public Constructor<T> getJavaConstructor() {
        return javaConstructor;
    }

    public T newInstance(Object... args) {
        return XReflection.execute(() -> javaConstructor.newInstance(args));
    }

}
