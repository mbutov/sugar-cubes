package org.sugarcubes.cloner;

import org.objenesis.strategy.StdInstantiatorStrategy;
import org.sugarcubes.builder.Builders;

import com.esotericsoftware.kryo.Kryo;

/**
 * The implementation of {@link Cloner} which uses Kryo serialization for cloning.
 *
 * @author Maxim Butov
 */
public class KryoCloner extends AbstractCloner {

    private final Kryo kryo;

    public KryoCloner() {
        this(Builders.of(Kryo::new)
            .apply(kryo -> kryo.setInstantiatorStrategy(new StdInstantiatorStrategy()))
            .build());
    }

    public KryoCloner(Kryo kryo) {
        this.kryo = kryo;
    }

    @Override
    protected Object doClone(Object object) throws Throwable {
        return kryo.copy(object);
    }

}
