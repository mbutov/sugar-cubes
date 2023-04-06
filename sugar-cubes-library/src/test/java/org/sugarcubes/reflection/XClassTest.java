package org.sugarcubes.reflection;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

/**
 * @author Maxim Butov
 */
public class XClassTest {

    @Test
    public void testCache() {
        XClass<Integer> xClass = XReflection.of(Integer.class);
        List<XMethod<?>> methods1 = xClass.getMethods().collect(Collectors.toList());
        List<XMethod<?>> methods2 = xClass.getMethods().collect(Collectors.toList());
        Assertions.assertEquals(methods1, methods2);
        Assertions.assertNotSame(methods1, methods2);
        for (int k = 0; k < methods1.size(); k++) {
            Assertions.assertSame(methods1.get(k), methods2.get(k));
        }
    }

    interface A {

    }

    interface B {

    }

    interface C extends B {

    }

    interface D extends C {

    }

    class E implements D, A {

    }

    interface U {

    }

    class V extends E implements U {

    }

    @Test
    public void testXxx() {

        assertThat(
            XReflection.of(D.class).getDeclaredInterfaces().collect(Collectors.toList()),
            containsInAnyOrder(Stream.of(C.class).map(XReflection::of).toArray())
        );
        assertThat(
            XReflection.of(D.class).getInterfaces().collect(Collectors.toList()),
            containsInAnyOrder(Stream.of(C.class).map(XReflection::of).toArray())
        );
        assertThat(
            XReflection.of(E.class).getDeclaredInterfaces().collect(Collectors.toList()),
            containsInAnyOrder(Stream.of(D.class, A.class).map(XReflection::of).toArray())
        );

//        System.out.println(Arrays.toString(D.class.getInterfaces()));
//        System.out.println(Arrays.toString(E.class.getInterfaces()));
//        System.out.println(Arrays.toString(V.class.getInterfaces()));
    }

}
