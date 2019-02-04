package org.sugarcubes.serialization;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectOutputStream extends DataOutputStream {

    final Map<Object, Integer> objects = new IdentityHashMap<>();
    final Map<String, Integer> strings = new IdentityHashMap<>();

    public XObjectOutputStream(OutputStream out) {
        super(out);
    }

    public void writeObject(Object object) throws IOException {
        if (object == null) {
            writeNull();
        }
        else if (object instanceof String) {
            writeString((String) object);
        }
        else {
            Integer index = objects.get(object);
            if (index != null) {
                writeReference(index);
            }
            else {
                objects.put(object, objects.size());
                writeNewObject(object);
            }
        }
    }

    public void writeNull() throws IOException {
        XStreamToken.NULL.write(this, null);
    }

    public void writeString(String string) throws IOException {
        Integer index = strings.get(string);
        if (index != null) {
            writeStringReference(index);
        }
        else {
            strings.put(string, strings.size());
            writeNewString(string);
        }
    }

    private void writeReference(int reference) throws IOException {
        XStreamToken.REFERENCE.write(this, reference);
    }

    private void writeNewObject(Object object) throws IOException {
    }

    private void writeStringReference(int reference) throws IOException {
        XStreamToken.STRING_REFERENCE.write(this, reference);
    }

    private void writeNewString(String string) throws IOException {
        XStreamToken.STRING.write(this, string);
    }

}
