package org.sugarcubes.cloner;

import org.sugarcubes.rex.Rex;
import static org.sugarcubes.rex.Rex.withMessage;

/**
 * Base abstract class for cloners.
 *
 * @author Maxim Butov
 */
public abstract class AbstractCloner implements Cloner {

    @Override
    public <T> T clone(T object) {
        try {
            return (T) doClone(object);
        }
        catch (Throwable e) {
            throw Rex.of(e)
                .rethrowIfError()
                .rethrowIf(ClonerException.class)
                .rethrow(withMessage("Unexpected error", ClonerException::new));
        }
    }

    /**
     * Performs cloning of the object.
     *
     * @param object object to clone, not null
     * @return clone of the object
     */
    protected abstract Object doClone(Object object) throws Throwable;

}
