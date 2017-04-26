package org.sugarcubes.laser;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Extension of {@link ObjectOutputStream} which can write non-serializable lambdas.
 *
 * @author Maxim Butov
 */
public class LambdaObjectOutputStream extends ObjectOutputStream {

    protected LambdaObjectOutputStream() throws IOException, SecurityException {
        super();
        enableReplaceObject(true);
    }

    public LambdaObjectOutputStream(OutputStream out) throws IOException {
        super(out);
        enableReplaceObject(true);
    }

    @Override
    protected Object replaceObject(Object obj) throws IOException {
        if (shouldReplace(obj)) {
            return doReplace(obj);
        }
        return super.replaceObject(obj);
    }

    private static class LambdaSerializableProxy implements Serializable {

        private static final long serialVersionUID = 1L;

        private LambdaSerializable serializable;

        public LambdaSerializableProxy(Object lambda) {
            serializable = new LambdaSerializable(lambda);
        }

        private Object readResolve() throws ObjectStreamException {
            return serializable.toLambda();
        }

    }

    protected boolean shouldReplace(Object obj) {
        return Laser.isNonSerializableLambda(obj);
    }

    protected Object doReplace(Object obj) {
        return new LambdaSerializableProxy(obj);
    }

}
