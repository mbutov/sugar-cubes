package org.sugarcubes.mixin;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public interface AgeMixin extends BeanMixin {

    default int getAge() {
        return getProperty("age", 0);
    }

    default void setAge(int age) {
        setProperty("age", age);
    }

}
