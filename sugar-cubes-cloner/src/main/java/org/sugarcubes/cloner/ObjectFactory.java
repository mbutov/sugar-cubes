package org.sugarcubes.cloner;

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
    <T> T newInstance(Class<T> clazz) throws ClonerException;

}
