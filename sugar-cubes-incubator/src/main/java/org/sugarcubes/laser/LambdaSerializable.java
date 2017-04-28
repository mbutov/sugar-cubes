package org.sugarcubes.laser;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

/**
 * A serializable form of a lambda.
 * Can be converted back with {@link #toLambda()} method.
 *
 * @author Maxim Butov
 */
public class LambdaSerializable implements Serializable {

    private Object lambda;

    public LambdaSerializable(Object lambda) {
        SerializedLambda serializedLambda = new SerializedLambda(LambdaSerializable.class,
            "", "", "",
            0, "", "", "",
            "", new Object[] {this,});
        lambda = serializedLambda;
    }

    public Object toLambda() {
        return lambda;
    }

    private static Object $deserializeLambda$(SerializedLambda serializedLambda) {
        LambdaSerializable lambdaSerializable = (LambdaSerializable) serializedLambda.getCapturedArg(0);
        return (Runnable) () -> {};
    }

}
