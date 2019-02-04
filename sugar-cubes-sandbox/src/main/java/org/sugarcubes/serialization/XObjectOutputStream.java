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
    private final Map<Object, Integer> strings = new HashMap<>();

    public XObjectOutputStream(OutputStream out) {
        super(out);
    }

    public void writeObject(Object object) throws IOException {
        if (object == null) {
            writeNewObject(null);
        }
        else {
            Integer index = objects.get(object);
            if (index == null) {
                index = strings.get(object);
            }
            if (index != null) {
                writeReference(index);
            }
            else {
                index = objects.size();
                objects.put(object, index);
                if (object instanceof String) {
                    strings.put(object, index);
                }
                writeNewObject(object);
            }
        }
    }

    private void writeReference(int reference) throws IOException {
        XStreamToken.REFERENCE.write(this, reference);
    }

    private void writeNewObject(Object object) throws IOException {
        XStreamToken.forValue(object).write(this, object);
    }

}
