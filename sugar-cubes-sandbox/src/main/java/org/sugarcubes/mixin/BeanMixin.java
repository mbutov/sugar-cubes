package org.sugarcubes.mixin;

import java.util.Map;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public interface BeanMixin {

    Map<String, Object> getProperties();

    default <T> void setProperty(String name, T value) {
        getProperties().put(name, value);
    }

    default <T> T getProperty(String name, T ifAbsent) {
        return (T) getProperties().getOrDefault(name, ifAbsent);
    }

    default <T> T getProperty(String name) {
        return getProperty(name, null);
    }

    default void copyFrom(BeanMixin other) {
        getProperties().putAll(other.getProperties());
    }

}
