package org.sugarcubes.laser;

import java.io.Serializable;

/**
 * A serializable form of a lambda.
 * Can be converted back with {@link #toLambda()} method.
 *
 * @author Maxim Butov
 */
public class LambdaSerializable implements Serializable {

    private Object lambda;

    public LambdaSerializable(Object lambda) {
        
    }

    public Object toLambda() {
        return lambda;
    }

}
