package org.sugarcubes.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Maxim Butov
 */
public abstract class SmartInvocationHandler implements InvocationHandler {

    private Set<Class> interfaces = new HashSet<Class>();

    protected void interceptMethodsOf(Class... interfaces) {
        for (Class anInterface : interfaces) {
            if (!anInterface.isInstance(this)) {
                throw new IllegalArgumentException(this + " is not a " + anInterface.getName());
            }
            this.interfaces.add(anInterface);
        }
    }

    protected abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (interfaces.contains(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        else {
            return doInvoke(proxy, method, args);
        }
    }

}
