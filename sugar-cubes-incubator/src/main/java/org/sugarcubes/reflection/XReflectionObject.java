package org.sugarcubes.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class XReflectionObject<T extends AccessibleObject> {

    private final T javaObject;

    public XReflectionObject(T javaObject) {
        this.javaObject = javaObject;
    }

    public T getJavaObject() {
        return javaObject;
    }

}
