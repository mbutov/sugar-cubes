package org.sugarcubes.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectInputStream extends DataInputStream {

    public XObjectInputStream(InputStream in) {
        super(in);
    }

    public Object readObject() throws IOException {
        return null;
    }

}
