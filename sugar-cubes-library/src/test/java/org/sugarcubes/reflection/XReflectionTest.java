package org.sugarcubes.reflection;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XReflectionTest {

    @Test
    public void testClasses() {

        XClass<Integer> xClass = XReflection.of(Integer.class);

        assertThat(xClass.getConstructors().count(), greaterThan(0L));
        assertThat(xClass.getFields().count(), greaterThan(0L));
        assertThat(xClass.getFields().count(), greaterThan(xClass.getDeclaredFields().count()));
        assertThat(xClass.getMethods().count(), greaterThan(0L));
        assertThat(xClass.getMethods().count(), greaterThan(xClass.getDeclaredMethods().count()));

        Set<XField<?>> collect = xClass.getFields().filter(xField -> !xField.isPublic()).collect(Collectors.toSet());

    }

    @Test
    public void testInheritance() {

        List<Class> inheritance =
            XReflection.of(Integer.class).getInheritance().map(XClass::getReflectionObject).collect(Collectors.toList());
        assertThat(inheritance, is(Arrays.asList(Integer.class, Number.class, Object.class)));

    }

    @Test
    public void testFinal() {

        XField<Integer> xField = XReflection.of(Integer.class).<Integer>getField("value").withNoFinal();

        Integer obj = new Integer(-1);
        Integer newValue = 1;
        xField.set(obj, newValue);

        assertEquals(newValue, xField.get(obj));

    }

    @Test
    public void testModifiers() {

        XClass<Integer> xClass = XReflection.of(Integer.class);

        assertTrue(xClass.isPublic());
        assertTrue(xClass.isFinal());

        assertFalse(xClass.isAbstract());
        assertFalse(xClass.isInterface());
        assertFalse(xClass.isPackage());
        assertFalse(xClass.isPrivate());
        assertFalse(xClass.isProtected());

        assertThat(xClass.getXModifiers(), containsInAnyOrder(XModifier.PUBLIC, XModifier.FINAL));

        XField<Object> xField = xClass.getDeclaredField("serialVersionUID");

        assertTrue(xField.isPrivate());
        assertTrue(xField.isStatic());
        assertTrue(xField.isFinal());

        assertThat(xField.getXModifiers(), containsInAnyOrder(XModifier.PRIVATE, XModifier.STATIC, XModifier.FINAL));

    }

}
