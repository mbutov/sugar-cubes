package org.sugarcubes.reflection;

import java.lang.reflect.Method;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XMethod<T> extends XExecutable<T> {

    private final Method javaMethod;

    public XMethod(Method javaMethod) {
        this.javaMethod = XReflection.prepare(javaMethod);
    }

    public Method getJavaMethod() {
        return javaMethod;
    }

    public T execute(Object _this, Object... args) {
        return XReflection.execute(() -> javaMethod.invoke(_this, args));
    }

    public T executeStatic(Object... args) {
        return execute(null, args);
    }

}
