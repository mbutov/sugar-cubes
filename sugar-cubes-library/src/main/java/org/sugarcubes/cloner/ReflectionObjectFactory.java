package org.sugarcubes.cloner;

import org.sugarcubes.reflection.XReflection;

/**
 * Object factory which uses no-arg constructor to create object.
 *
 * @author Maxim Butov
 */
public class ReflectionObjectFactory implements ClonerObjectFactory {

    @Override
    public <T> T newInstanceUnsafe(Class<T> clazz) throws Throwable {
        return XReflection.of(clazz).getConstructor().newInstance();
    }

}
