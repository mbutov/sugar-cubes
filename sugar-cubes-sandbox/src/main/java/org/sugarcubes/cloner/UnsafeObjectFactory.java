package org.sugarcubes.cloner;

import sun.misc.Unsafe;

/**
 * Object factory which uses {@link Unsafe} to create object instance.
 *
 * @author Maxim Butov
 */
public class UnsafeObjectFactory implements ObjectFactory {

    @Override
    public <T> T newInstanceUnsafe(Class<T> clazz) throws Throwable {
        return (T) UnsafeUtils.getUnsafe().allocateInstance(clazz);
    }

}
