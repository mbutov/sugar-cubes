package org.sugarcubes.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectOutputStream extends DataOutputStream {

    private final Map<Object, Integer> objects = new IdentityHashMap<>();
    private final Map<Object, Integer> immutables = new HashMap<>();

    boolean isImmutable(Object object) {
        return object instanceof String;
    }

    int getReference(Object object) {
        Integer ref = objects.get(object);
        if (ref == null) {
            ref = immutables.get(object);
        }
        return ref != null ? ref : -1;
    }

    int putReference(Object object) {
        int ref = objects.size();
        objects.put(object, ref);
        if (isImmutable(object)) {
            immutables.put(object, ref);
        }
        return ref;
    }

    public XObjectOutputStream(OutputStream out) {
        super(out);
    }

    public void writeObject(Object object) throws IOException {
        for (XSerializer serializer : XSerializers.values()) {
            if (serializer.write(this, object)) {
                return;
            }
        }
        throw new IllegalStateException();
    }

}
