package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;

/**
 * @author Maxim Butov
 */
public class XReferenceSerializer implements XSerializer<Object> {

    @Override
    public int tag() {
        return 'R';
    }

    @Override
    public boolean write(XObjectOutputStream out, Object value) throws IOException {
        int reference = out.findReference(value);
        if (reference != -1) {
            writeTag(out);
            out.writeInt(reference);
            return true;
        }
        else {
            out.addReference(value);
            return false;
        }
    }

    @Override
    public Object create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        return in.getByReference(in.readInt());
    }

}
