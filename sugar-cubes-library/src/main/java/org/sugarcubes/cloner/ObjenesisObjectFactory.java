package org.sugarcubes.cloner;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * Object factory which uses Objenesis library to create objects.
 *
 * @author Maxim Butov
 */
public class ObjenesisObjectFactory implements ObjectFactory {

    private Objenesis objenesis;

    public ObjenesisObjectFactory() {
        this(new ObjenesisStd());
    }

    public ObjenesisObjectFactory(Objenesis objenesis) {
        this.objenesis = objenesis;
    }

    @Override
    public <T> T newInstanceUnsafe(Class<T> clazz) throws Throwable {
        return objenesis.newInstance(clazz);
    }

}
