package org.sugarcubes.s2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

/**
 * @author Maxim Butov
 */
class ZObjectInputStreamTest {

    enum E {
        a,
        b {
            @Override
            public String toString() {
                return super.toString();
            }
        },
    }

    static class TestInvocationHandler implements InvocationHandler, Serializable {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }

    }

    @Test
    void testZInputStream() throws Exception {
//        testZInputStream(1);
//        testZInputStream(false, (byte) 1, (short) 2, (char) 3, 4, 5l, 6.0f, 7.0d);
//        testZInputStream((Object) null);
//        testZInputStream(null, null);
//        testZInputStream("");
//        testZInputStream("a");
//        testZInputStream("a", "b");
//        testZInputStream("a", "a", "b", "b", "a", "b");
//        testZInputStream(StringUtils.repeat('a', 65536));
//
//        testZInputStream((Object) new Object[0]);
//        testZInputStream((Object) new Object[2]);
//
//        testZInputStream((Object) new Integer[0]);
//        testZInputStream((Object) new Integer[2]);
//        testZInputStream((Object) new Integer[] {1, 2});
//
//        testZInputStream(new byte[0]);
//        testZInputStream(new byte[2]);
//        testZInputStream(new byte[] {1, -1});
//
//        testZInputStream(new boolean[0]);
//        testZInputStream(new boolean[2]);
//        testZInputStream(new boolean[] {false, true});
//
//        testZInputStream(new short[0]);
//        testZInputStream(new short[2]);
//        testZInputStream(new short[] {1, -1});
//
//        testZInputStream(new char[0]);
//        testZInputStream(new char[2]);
//        testZInputStream(new char[] {'a', '\u9999'});
//
//        testZInputStream(new int[0]);
//        testZInputStream(new int[2]);
//        testZInputStream(new int[] {1, -1});
//
//        testZInputStream(new long[0]);
//        testZInputStream(new long[2]);
//        testZInputStream(new long[] {1, -1});
//
//        testZInputStream(new float[0]);
//        testZInputStream(new float[2]);
//        testZInputStream(new float[] {1, -1});
//
//        testZInputStream(new double[0]);
//        testZInputStream(new double[2]);
//        testZInputStream(new double[] {1, -1});
//
//        testZInputStream(E.a);
//        testZInputStream(E.a, E.a);
//        testZInputStream(E.a, E.b);
//
//        testZInputStream((Object) new int[][][] {{{1}}, {{2, 3,}, {4, 5}}});
//
//        testZInputStream(Integer.class);
//        testZInputStream(Integer.class, Integer.class);
//        testZInputStream(Integer.class, Integer.class, Integer.class);
//        testZInputStream(Integer.class, Byte.class, Integer.class);
//        testZInputStream(Integer.class, Byte.class, Integer.class, Byte.class);
//
//        testZInputStream(new byte[1024 * 1024 + 1]);
//
//        Object proxy = Proxy.newProxyInstance(null, new Class[] {Serializable.class, Cloneable.class},
//            new TestInvocationHandler());
//
//        testZInputStream(proxy);
//        testZInputStream(proxy.getClass());

        testZInputStream(MethodType.methodType(Object.class));
//        testZInputStream(Instant.now());

//        testZInputStream(new Throwable().fillInStackTrace());

    }

    void testZInputStream(Object... objects) throws Exception {
        byte[] bytes = write(stream -> new ObjectOutputStream(stream) {
            @Override
            protected void annotateClass(Class<?> cl) throws IOException {
                writeLong(123L);
            }
        }, objects);
        ZObjectInputStream zin = new ZObjectInputStream(new ByteArrayInputStream(bytes));
        for (Object obj1 : objects) {
            Object obj2 = zin.readObject();
            MatcherAssert.assertThat(SerializationUtils.serialize(obj2), Matchers.is(SerializationUtils.serialize(obj1)));
        }
        Assertions.assertEquals(-1, zin.read());
    }

    byte[] write(UnsafeFunction<OutputStream, ObjectOutput, IOException> stream, Object... objects) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput objectOutputStream = stream.apply(buffer);
        for (Object object : objects) {
            objectOutputStream.writeObject(object);
        }
        return buffer.toByteArray();
    }

    @FunctionalInterface
    interface UnsafeFunction<X, Y, E extends Throwable> {

        Y apply(X x) throws E;

    }

}
