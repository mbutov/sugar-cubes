package org.sugarcubes.cloner;

import org.sugarcubes.rex.Rex;
import static org.sugarcubes.rex.Rex.withMessage;

/**
 * Object factory interface.
 *
 * @author Maxim Butov
 */
public interface ClonerObjectFactory {

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
                .rethrowIfError()
                .rethrowIf(ClonerException.class)
                .rethrow(withMessage("Unexpected error", ClonerException::new));
        }
    }

    default <T> T newInstanceUnsafe(Class<T> clazz) throws Throwable {
        throw new AssertionError("Not implemented");
    }

}
