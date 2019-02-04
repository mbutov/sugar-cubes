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
public class XDataOutputStream extends DataOutputStream {

    private Map<Object, Integer> objects = new IdentityHashMap<>();
    private Map<String, Integer> strings = new IdentityHashMap<>();

    public XDataOutputStream(OutputStream out) {
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

    private void writeNull() throws IOException {
    }

    private void writeString(String string) throws IOException {
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
    }

    private void writeNewObject(Object object) throws IOException {
    }

    private void writeStringReference(int reference) throws IOException {
        writeByte('S');
        writeInt(reference);
    }

    private void writeNewString(String string) throws IOException {
        writeByte('S');
        writeUTF(string);
    }

}
