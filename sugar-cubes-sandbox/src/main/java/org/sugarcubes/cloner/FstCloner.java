package org.sugarcubes.cloner;

import org.nustaq.serialization.simpleapi.DefaultCoder;
import org.nustaq.serialization.simpleapi.FSTCoder;

/**
 * The implementation of {@link Cloner} which uses FST serialization for cloning.
 *
 * @author Maxim Butov
 */
public class FstCloner extends AbstractCloner {

    private final FSTCoder coder;

    public FstCloner() {
        this(new DefaultCoder());
    }

    public FstCloner(FSTCoder coder) {
        this.coder = coder;
    }

    @Override
    protected Object doClone(Object object) throws Throwable {
        return coder.toObject(coder.toByteArray(object));
    }

}
