package org.sugarcubes.s2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

public class ZXxxTest {

    static class A implements Serializable {

        static final long serialVersionUID = 42L;

        private final synchronized void writeObject(java.io.ObjectOutputStream out)
            throws Throwable {
            System.out.println("A.writeObject");
        }

        public Object writeReplace() {
            System.out.println("A.writeReplace");
            return this;
        }

    }

    @Test
    void testReplace() throws Exception {
        ZSerialInfo serialInfo = ZSerialInfo.of(A.class);
        System.out.println(serialInfo);
        byte[] bytes = SerializationUtils.serialize(new A());
        Object object = new ObjectInputStream(new ByteArrayInputStream(bytes))
            .readObject();
        System.out.println(object);
    }
}
