package org.sugarcubes.mixin;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public interface NameMixin extends BeanMixin {

    default String getName() {
        return getProperty("name");
    }

    default void setName(String name) {
        setProperty("name", name);
    }

}
