package org.sugarcubes.reflection;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XReflectionTest {

    @Test
    public void testClasses() {

        XClass<Integer> xClass = XReflection.of(Integer.class);

        Assert.assertThat(xClass.getConstructors().count(), greaterThan(0L));
        Assert.assertThat(xClass.getFields().count(), greaterThan(0L));
        Assert.assertThat(xClass.getMethods().count(), greaterThan(0L));


        Set<XField<?>> collect = xClass.getFields().filter(xField -> !xField.isPublic()).collect(Collectors.toSet());
    }

    @Test
    public void testInheritance() {

        List<Class> inheritance =
            XReflection.of(Integer.class).getInheritance().map(XClass::getReflectionObject).collect(Collectors.toList());
        Assert.assertThat(inheritance, is(Arrays.asList(Integer.class, Number.class, Object.class)));

    }

    @Test
    public void testFinal() {

        XField<Integer> xField = XReflection.of(Integer.class).getField("value");
        xField.clearFinal();

        Integer obj = new Integer(-1);
        Integer newValue = 1;
        xField.set(obj, newValue);
        
        Assert.assertEquals(newValue, xField.get(obj));

    }

    @Test
    public void testModifiers() {

        XClass<Integer> xClass = XReflection.of(Integer.class);

        Assert.assertTrue(xClass.isPublic());
        Assert.assertTrue(xClass.isFinal());

        Assert.assertFalse(xClass.isAbstract());
        Assert.assertFalse(xClass.isInterface());
        Assert.assertFalse(xClass.isPackage());
        Assert.assertFalse(xClass.isPrivate());
        Assert.assertFalse(xClass.isProtected());

        XField<Object> xField = xClass.getDeclaredField("serialVersionUID");

        Assert.assertTrue(xField.isPrivate());
        Assert.assertTrue(xField.isStatic());
        Assert.assertTrue(xField.isFinal());

    }

}
