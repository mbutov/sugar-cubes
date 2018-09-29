package org.sugarcubes.reflection;

import org.junit.Test;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XReflectionTest {

    public static class A {

        private int a = 1;
        protected int b = 2;
        public int c = 3;
        int d = 4;

    }

    @Test
    public void testClasses() {

        XClass<A> xClass = new XClass(A.class);
        A a = new A();

        xClass.getAllFields().forEach(System.out::println);
        xClass.getAllFields().filter(XModifiers::isPublic).forEach(System.out::println);

    }

}
