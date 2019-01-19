package org.sugarcubes.cloner;

import org.nustaq.serialization.FSTConfiguration;

/**
 * The implementation of {@link Cloner} which uses FST serialization for cloning.
 *
 * @author Maxim Butov
 */
public class FstCloner extends AbstractCloner {

    private final FSTConfiguration configuration;

    public FstCloner() {
        this(FSTConfiguration.createDefaultConfiguration());
    }

    public FstCloner(FSTConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Object doClone(Object object) throws Throwable {
        return configuration.asObject(configuration.asByteArray(object));
    }

}
