package org.sugarcubes.proxy;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author Maxim Butov
 */
public abstract class AbstractProxyProviderTests {

    protected abstract DefaultProxyProvider getProxyProvider();

    static interface TestInterface {

        String test(String arg);

    }

    @Test
    public void testInterfaceProxy() {

        DefaultProxyProvider proxyProvider = getProxyProvider();
        proxyProvider.setUseJavaProxiesIfPossible(true);

        Assert.assertFalse(proxyProvider.isProxy(new Object()));

        ProxyFactory<TestInterface> proxyFactory = proxyProvider.getFactory(TestInterface.class);
        Assert.assertSame(proxyFactory, proxyProvider.getFactory(TestInterface.class));
        Class<? extends TestInterface> proxyClass = proxyFactory.getProxyClass();
        System.out.println("proxyClass = " + proxyClass);
        System.out.println("ProxyUtils.maybeProxy(proxyClass) = " + ProxyUtils.maybeProxy(proxyClass));
        Assert.assertTrue(ProxyUtils.isProxyClass(proxyClass));

        Assert.assertFalse(proxyClass.isInterface());
        Assert.assertTrue(TestInterface.class.isAssignableFrom(proxyClass));

        Assert.assertTrue(java.lang.reflect.Proxy.isProxyClass(proxyClass));
        Assert.assertFalse(Enhancer.isEnhanced(proxyClass));

        InvocationHandler handler1 = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return "x" + args[0];
            }
        };
        TestInterface proxy1 = proxyFactory.newProxy(handler1);
        Assert.assertSame(proxyProvider.getInvocationHandler(proxy1), handler1);
        Assert.assertTrue(proxyProvider.isProxy(proxy1));
        Assert.assertSame(proxy1.getClass(), proxyClass);
        Assert.assertNotSame(proxy1, proxyFactory.newProxy(handler1));
        Assert.assertEquals("xx", proxy1.test("x"));
    }

    static class TestClass implements TestInterface {

        // test class with no visible constructors
        private TestClass(boolean unused) {
        }

        @Override
        public String test(String arg) {
            return "test";
        }

        public final String testFinal(String arg) {
            return "final";
        }

        protected String testPrivate(String arg) {
            return "private";
        }

    }

    @Test
    public void testClassProxy() throws Exception {

        DefaultProxyProvider proxyProvider = getProxyProvider();
        proxyProvider.setUseJavaProxiesIfPossible(true);

        Assert.assertFalse(proxyProvider.isProxy(new Object()));

        ProxyFactory<TestClass> proxyFactory = proxyProvider.getFactory(TestClass.class);
        Assert.assertSame(proxyFactory, proxyProvider.getFactory(TestClass.class));

        Class<? extends TestInterface> proxyClass = proxyFactory.getProxyClass();
        System.out.println("proxyClass = " + proxyClass);
        System.out.println("ProxyUtils.maybeProxy(proxyClass) = " + ProxyUtils.maybeProxy(proxyClass));
        Assert.assertTrue(ProxyUtils.isProxyClass(proxyClass));

        Assert.assertFalse(proxyClass.isInterface());
        Assert.assertTrue(TestInterface.class.isAssignableFrom(proxyClass));
        Assert.assertTrue(TestClass.class.isAssignableFrom(proxyClass));

        Assert.assertFalse(java.lang.reflect.Proxy.isProxyClass(proxyClass));

        InvocationHandler handler1 = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return "x" + args[0];
            }
        };
        TestClass proxy1 = proxyFactory.newProxy(handler1);
        Assert.assertSame(proxyProvider.getInvocationHandler(proxy1), handler1);
        Assert.assertTrue(proxyProvider.isProxy(proxy1));
        Assert.assertSame(proxy1.getClass(), proxyClass);
        Assert.assertNotSame(proxy1, proxyFactory.newProxy(handler1));
        Assert.assertEquals("xx", proxy1.test("x"));
        Assert.assertEquals("final", proxy1.testFinal("x"));

    }

    static final class TestFinalClass {

    }

    @Test(expected = RuntimeException.class)
    public void testFinalClass() {

        getProxyProvider().getFactory(TestFinalClass.class);

    }

    static interface TestSerializableInterface extends Serializable {

    }

    static class TestSerializableClass implements TestSerializableInterface {

        Object writeReplace() throws ObjectStreamException {
            return new Object();
        }

    }

    static class SerializableInvocationHandlerImpl implements SerializableInvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @Test
    public void testSerializable() {

        DefaultProxyProvider proxyProvider = getProxyProvider();
        proxyProvider.setUseJavaProxiesIfPossible(true);

        ProxyFactory<TestSerializableInterface> factory1 = proxyProvider.getFactory(TestSerializableInterface.class);
        TestSerializableInterface proxy11 = factory1.newProxy(new SerializableInvocationHandlerImpl());
        TestSerializableInterface proxy12 = (TestSerializableInterface) SerializationUtils.deserialize(SerializationUtils.serialize(proxy11));
        Assert.assertNotSame(proxy11, proxy12);

        ProxyFactory<TestSerializableClass> factory2 = proxyProvider.getFactory(TestSerializableClass.class);
        TestSerializableClass proxy21 = factory2.newProxy(new SerializableInvocationHandlerImpl());
        TestSerializableClass proxy22 = (TestSerializableClass) SerializationUtils.deserialize(SerializationUtils.serialize(proxy21));
        Assert.assertNotSame(proxy21, proxy22);

    }

    @Test
    public void testAccess() throws Throwable {

        print(Class.forName("org.sugarcubes.otherpackage.PackageClass"));
        print(Class.forName("org.sugarcubes.otherpackage.PackageClass$InnerClass"));
        print(Class.forName("org.sugarcubes.otherpackage.PackageInterface"));
        print(Class.forName("java.util.RandomAccessSubList"));

    }

    private void print(Class clazz) {

        System.out.println(clazz.getName());

        System.out.println("isPublic = " + Modifier.isPublic(clazz.getModifiers()));
        System.out.println("isProtected = " + Modifier.isProtected(clazz.getModifiers()));
        System.out.println("isPrivate = " + Modifier.isPrivate(clazz.getModifiers()));

        System.out.println("isAnonymousClass = " + clazz.isAnonymousClass());
        System.out.println("isLocalClass = " + clazz.isLocalClass());
        System.out.println("isMemberClass = " + clazz.isMemberClass());
        System.out.println("isSynthetic = " + clazz.isSynthetic());

        System.out.println("getEnclosingClass = " + clazz.getEnclosingClass());

        try {
            getProxyProvider().getFactory(clazz).newProxy(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }
            });
            System.out.println("No error.");
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

}
