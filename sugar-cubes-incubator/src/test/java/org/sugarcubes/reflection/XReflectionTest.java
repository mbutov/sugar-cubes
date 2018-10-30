package org.sugarcubes.reflection;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.junit.Assert;
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

        private final int c = -1;

    }

    @Test
    public void testFinal() {

        C obj = new C();

        XField<Integer> xField = XReflection.of(obj.getClass()).getField("c");
        xField.clearFinal();

        Integer newValue = Integer.valueOf(1);
        xField.set(obj, newValue);
        
        Assert.assertEquals(newValue, xField.get(obj));

    }

    @Test
    public void zzz() {

        System.out.println(XReflection.of(BigDecimal.class).getInheritance().collect(Collectors.toList()));

    }

}
