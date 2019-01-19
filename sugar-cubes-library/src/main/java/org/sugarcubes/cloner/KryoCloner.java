package org.sugarcubes.cloner;

import com.esotericsoftware.kryo.Kryo;

/**
 * The implementation of {@link Cloner} which uses Kryo serialization for cloning.
 *
 * @author Maxim Butov
 */
public class KryoCloner extends AbstractCloner {

    private final Kryo kryo;

    public KryoCloner() {
        this(new Kryo());
    }

    public KryoCloner(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    protected Object doClone(Object object) throws Throwable {
        return kryo.copy(object);
    }

}
