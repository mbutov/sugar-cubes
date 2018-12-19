package org.sugarcubes.cloner;

import sun.misc.Unsafe;

/**
 * Object factory which uses {@link Unsafe} to create object instance.
 *
 * @author Maxim Butov
 */
public class UnsafeObjectFactory implements ObjectFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T newInstance(Class<T> clazz) {
        try {
            return (T) UnsafeUtils.getUnsafe().allocateInstance(clazz);
        }
        catch (InstantiationException e) {
            throw new ClonerException(e);
        }
    }

}
