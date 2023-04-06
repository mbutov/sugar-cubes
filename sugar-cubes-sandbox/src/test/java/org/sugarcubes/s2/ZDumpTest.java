package org.sugarcubes.s2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Test;
import org.unsynchronized.Getopt;
import org.unsynchronized.jdeserialize;

public class ZDumpTest {

    enum EEE {
        a,b,
    }

    @Test
    void testDumpObjects() throws Exception {
//        dump();
        dump(new Throwable().fillInStackTrace());
    }

    private void dump(Object ... objs) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(buf);
        for (Object obj : objs) {
            oos.writeObject(obj);
        }
        jdeserialize jd = new jdeserialize("stream");
        jd.run(new ByteArrayInputStream(buf.toByteArray()), true);
        Getopt options = new Getopt();
        options.parse(new String[]{});
        jd.dump(options);
    }

}
