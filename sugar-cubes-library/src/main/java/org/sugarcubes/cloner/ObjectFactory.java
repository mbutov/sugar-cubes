package org.sugarcubes.cloner;

import org.sugarcubes.rex.Rex;

/**
 * Object factory interface.
 *
 * @author Maxim Butov
 */
public interface ObjectFactory {

    /**
     * Creates an instance of the specified class. Instance may be not initialized.
     *
     * @param clazz class to instantiate
     * @return new instance of the class
     *
     * @throws ClonerException if something went wrong
     */
    default <T> T newInstance(Class<T> clazz) throws ClonerException {
        try {
            return newInstanceUnsafe(clazz);
        }
        catch (Throwable e) {
            throw Rex.of(e)
                .throwIfError()
                .throwIf(ClonerException.class)
                .throwOther(ClonerException::new);
        }
    }

    default <T> T newInstanceUnsafe(Class<T> clazz) throws Throwable {
        throw new Error("Must never happen");
    }

}
