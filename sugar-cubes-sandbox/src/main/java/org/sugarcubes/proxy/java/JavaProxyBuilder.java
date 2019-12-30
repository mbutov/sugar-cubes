package org.sugarcubes.proxy.java;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Maxim Butov
 */
public class JavaProxyBuilder<T> {

    interface ProxyInvocationHandler<T, R> {

        R invoke(T proxy, T target, Object[] args) throws Throwable;

    }

    interface NoArgHandler<T, R> extends ProxyInvocationHandler<T, R> {

        @Override
        default R invoke(T proxy, T target, Object[] args) throws Throwable {
            return invoke(proxy, target);
        }

        R invoke(T proxy, T target) throws Throwable;

    }

    interface NoArgVoidHandler<T> extends NoArgHandler<T, Object> {

        void call(T proxy, T target) throws Throwable;

        @Override
        default Object invoke(T proxy, T target) throws Throwable {
            call(proxy, target);
            return null;
        }

    }

    interface OneArgHandler<T, R, U> extends ProxyInvocationHandler<T, R> {

        R invoke(T proxy, T target, U arg0) throws Throwable;

        @Override
        default R invoke(T proxy, T target, Object[] args) throws Throwable {
            return invoke(proxy, target, (U) args[0]);
        }

    }

    interface OneArgVoidHandler<T, U> extends OneArgHandler<T, Object, U> {

        void call(T proxy, T target, U arg0) throws Throwable;

        @Override
        default Object invoke(T proxy, T target, U arg0) throws Throwable {
            call(proxy, target, arg0);
            return null;
        }

    }

    interface TwoArgsHandler<T, R, U, V> extends ProxyInvocationHandler<T, R> {

        R invoke(T proxy, T target, U arg0, V arg1) throws Throwable;

        @Override
        default R invoke(T proxy, T target, Object[] args) throws Throwable {
            return invoke(proxy, target, (U) args[0], (V) args[1]);
        }

    }

    interface TwoArgsVoidHandler<T, U, V> extends TwoArgsHandler<T, Object, U, V> {

        void call(T proxy, T target, U arg0, V arg1) throws Throwable;

        @Override
        default Object invoke(T proxy, T target, U arg0, V arg1) throws Throwable {
            call(proxy, target, arg0, arg1);
            return null;
        }

    }

    interface ThreeArgsHandler<T, R, U, V, W> extends ProxyInvocationHandler<T, R> {

        R invoke(T proxy, T target, U arg0, V arg1, W arg2) throws Throwable;

        @Override
        default R invoke(T proxy, T target, Object[] args) throws Throwable {
            return invoke(proxy, target, (U) args[0], (V) args[1], (W) args[2]);
        }

    }

    interface ThreeArgsVoidHandler<T, U, V, W> extends ThreeArgsHandler<T, Object, U, V, W> {

        void call(T proxy, T target, U arg0, V arg1, W arg2) throws Throwable;

        @Override
        default Object invoke(T proxy, T target, U arg0, V arg1, W arg2) throws Throwable {
            call(proxy, target, arg0, arg1, arg2);
            return null;
        }

    }

    private static class MethodHandler {

        private final ProxyInvocationHandler handler;
        private final String methodName;
        private final Collection<Class<?>> parameterTypes;

        public MethodHandler(ProxyInvocationHandler handler, String methodName, Class<?>... parameterTypes) {
            this.handler = handler;
            this.methodName = methodName;
            this.parameterTypes = Arrays.asList(parameterTypes);
        }

        public boolean matches(Method method) {
            return method.getName().equals(methodName) && parameterTypes.equals(Arrays.asList(method.getParameterTypes()));
        }

        public ProxyInvocationHandler getHandler() {
            return handler;
        }

    }

    private ClassLoader classLoader = getClass().getClassLoader();
    private Set<Class<?>> interfaces = new HashSet<>();
    private List<MethodHandler> handlers = new ArrayList<>();

    private static List<Class<?>> getAllInterfaces(Class<?> clazz) {
        if (clazz.isInterface()) {
            return Collections.singletonList(clazz);
        }
        List<Class<?>> interfaces = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            interfaces.addAll(Arrays.asList(c.getInterfaces()));
        }
        return interfaces;
    }

    public JavaProxyBuilder<T> classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public JavaProxyBuilder<T> interfaces(Class<?>... interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));
        return this;
    }

    private JavaProxyBuilder<T> method(ProxyInvocationHandler handler, String methodName, Class<?>... parameterTypes) {
        handlers.add(new MethodHandler(handler, methodName, parameterTypes));
        return this;
    }

    public <R> JavaProxyBuilder<T> method(String methodName, NoArgHandler<T, R> handler) {
        return method(handler, methodName);
    }

    public JavaProxyBuilder<T> voidMethod(String methodName, NoArgVoidHandler<T> handler) {
        return method(handler, methodName);
    }

    public <R, U> JavaProxyBuilder<T> method(String methodName, Class<U> arg0, OneArgHandler<T, R, U> handler) {
        return method(handler, methodName, arg0);
    }

    public <U> JavaProxyBuilder<T> voidMethod(String methodName, Class<U> arg0, OneArgVoidHandler<T, U> handler) {
        return method(handler, methodName, arg0);
    }

    public <R, U, V> JavaProxyBuilder<T> method(String methodName, Class<U> arg0, Class<V> arg1, TwoArgsHandler<T, R, U, V> handler) {
        return method(handler, methodName, arg0, arg1);
    }

    public <U, V> JavaProxyBuilder<T> voidMethod(String methodName, Class<U> arg0, Class<V> arg1, TwoArgsVoidHandler<T, U, V> handler) {
        return method(handler, methodName, arg0, arg1);
    }

    public <R, U, V, W> JavaProxyBuilder<T> method(String methodName, Class<U> arg0, Class<V> arg1, Class<W> arg2, ThreeArgsHandler<T, R, U, V, W> handler) {
        return method(handler, methodName, arg0, arg1, arg2);
    }

    public <U, V, W> JavaProxyBuilder<T> voidMethod(String methodName, Class<U> arg0, Class<V> arg1, Class<W> arg2, ThreeArgsVoidHandler<T, U, V, W> handler) {
        return method(handler, methodName, arg0, arg1, arg2);
    }

    public JavaProxyBuilder<T> proxyEquals() {
        return method("equals", Object.class, (proxy, target, arg0) -> proxy == arg0);
    }

    public JavaProxyBuilder<T> proxyHashCode() {
        return method("hashCode", (proxy, target) -> System.identityHashCode(proxy));
    }

    public JavaProxyBuilder<T> proxyToString() {
        return method("toString", (proxy, target) -> String.format("Proxy %s of %s", interfaces, target));
    }

    public T newProxy(T target) {
        Set<Class<?>> interfaces = new HashSet<>(this.interfaces);
        if (target != null) {
            interfaces.addAll(getAllInterfaces(target.getClass()));
        }
        Object newProxy = Proxy.newProxyInstance(classLoader, interfaces.toArray(new Class[0]),
            (proxy, method, args) -> {
                for (MethodHandler handler : handlers) {
                    if (handler.matches(method)) {
                        return handler.getHandler().invoke(proxy, target, args);
                    }
                }
                if (target == null) {
                    throw new IllegalStateException("No handler for " + method);
                }
                return method.invoke(target, args);
            });
        return (T) newProxy;
    }

    public T newProxy() {
        return newProxy(null);
    }

}
