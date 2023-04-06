package org.sugarcubes.s2;

import java.io.FilterInputStream;
import java.io.InputStream;

public class ZProxyInputStream extends FilterInputStream {

    public ZProxyInputStream(InputStream in) {
        super(in);
    }

    public void setInput(InputStream input) {
        in = input;
    }

}
