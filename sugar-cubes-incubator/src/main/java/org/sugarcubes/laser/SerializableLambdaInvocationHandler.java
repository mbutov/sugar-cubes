package org.sugarcubes.laser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Maxim Butov
 */
public class SerializableLambdaInvocationHandler implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 1L;

    private transient Object lambda;

    public SerializableLambdaInvocationHandler(Object lambda) {
        this.lambda = lambda;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(lambda, args);
    }

    private LambdaSerializable serializable;

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (serializable == null) {
            serializable = new LambdaSerializable(lambda);
        }
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        lambda = serializable.toLambda();
    }

}
