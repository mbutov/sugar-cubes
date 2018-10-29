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

    public static class B {

    }

    @Test
    public void testClasses() {

        XClass<A> xClass = XReflection.of(A.class);
        A a = new A();

        xClass.getConstructors().forEach(System.out::println);
        xClass.getFields().forEach(System.out::println);
        xClass.getMethods().forEach(System.out::println);

    }

    public static class C {

        private final int c = 111;

    }

    @Test
    public void testFinal() {

        XField<Integer> xField = XReflection.of(C.class).getField("c");
        xField.clearFinal();
        C obj = new C();
        xField.set(obj, 1);
        System.out.println(xField.get(obj));

    }

}
