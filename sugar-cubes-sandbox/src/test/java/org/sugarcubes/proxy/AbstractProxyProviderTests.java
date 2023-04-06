package org.sugarcubes.proxy;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

        Assertions.assertFalse(proxyProvider.isProxy(new Object()));

        ProxyFactory<TestInterface> proxyFactory = proxyProvider.getFactory(TestInterface.class);
        Assertions.assertSame(proxyFactory, proxyProvider.getFactory(TestInterface.class));
        Class<? extends TestInterface> proxyClass = proxyFactory.getProxyClass();
        System.out.println("proxyClass = " + proxyClass);
        System.out.println("ProxyUtils.maybeProxy(proxyClass) = " + ProxyUtils.maybeProxy(proxyClass));
        Assertions.assertTrue(ProxyUtils.isProxyClass(proxyClass));

        Assertions.assertFalse(proxyClass.isInterface());
        Assertions.assertTrue(TestInterface.class.isAssignableFrom(proxyClass));

        Assertions.assertTrue(java.lang.reflect.Proxy.isProxyClass(proxyClass));
        Assertions.assertFalse(Enhancer.isEnhanced(proxyClass));

        InvocationHandler handler1 = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return "x" + args[0];
            }
        };
        TestInterface proxy1 = proxyFactory.newProxy(handler1);
        Assertions.assertSame(proxyProvider.getInvocationHandler(proxy1), handler1);
        Assertions.assertTrue(proxyProvider.isProxy(proxy1));
        Assertions.assertSame(proxy1.getClass(), proxyClass);
        Assertions.assertNotSame(proxy1, proxyFactory.newProxy(handler1));
        Assertions.assertEquals("xx", proxy1.test("x"));
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

        Assertions.assertFalse(proxyProvider.isProxy(new Object()));

        ProxyFactory<TestClass> proxyFactory = proxyProvider.getFactory(TestClass.class);
        Assertions.assertSame(proxyFactory, proxyProvider.getFactory(TestClass.class));

        Class<? extends TestInterface> proxyClass = proxyFactory.getProxyClass();
        System.out.println("proxyClass = " + proxyClass);
        System.out.println("ProxyUtils.maybeProxy(proxyClass) = " + ProxyUtils.maybeProxy(proxyClass));
        Assertions.assertTrue(ProxyUtils.isProxyClass(proxyClass));

        Assertions.assertFalse(proxyClass.isInterface());
        Assertions.assertTrue(TestInterface.class.isAssignableFrom(proxyClass));
        Assertions.assertTrue(TestClass.class.isAssignableFrom(proxyClass));

        Assertions.assertFalse(java.lang.reflect.Proxy.isProxyClass(proxyClass));

        InvocationHandler handler1 = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return "x" + args[0];
            }
        };
        TestClass proxy1 = proxyFactory.newProxy(handler1);
        Assertions.assertSame(proxyProvider.getInvocationHandler(proxy1), handler1);
        Assertions.assertTrue(proxyProvider.isProxy(proxy1));
        Assertions.assertSame(proxy1.getClass(), proxyClass);
        Assertions.assertNotSame(proxy1, proxyFactory.newProxy(handler1));
        Assertions.assertEquals("xx", proxy1.test("x"));
        Assertions.assertEquals("final", proxy1.testFinal("x"));

    }

    static final class TestFinalClass {

    }

    @Test
    public void testFinalClass() {
                 Assertions.assertThrows(RuntimeException.class, () ->
        getProxyProvider().getFactory(TestFinalClass.class));

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
        Assertions.assertNotSame(proxy11, proxy12);

        ProxyFactory<TestSerializableClass> factory2 = proxyProvider.getFactory(TestSerializableClass.class);
        TestSerializableClass proxy21 = factory2.newProxy(new SerializableInvocationHandlerImpl());
        TestSerializableClass proxy22 = (TestSerializableClass) SerializationUtils.deserialize(SerializationUtils.serialize(proxy21));
        Assertions.assertNotSame(proxy21, proxy22);

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
